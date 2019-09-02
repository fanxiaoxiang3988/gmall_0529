package com.atguigu.gmall.search.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.manager.BaseAttrInfo;
import com.atguigu.gmall.manager.SkuEsService;
import com.atguigu.gmall.manager.SkuService;
import com.atguigu.gmall.manager.es.SkuBaseAttrEsVo;
import com.atguigu.gmall.manager.es.SkuInfoEsVo;
import com.atguigu.gmall.manager.es.SkuSearchParamEsVo;
import com.atguigu.gmall.manager.es.SkuSearchResultEsVo;
import com.atguigu.gmall.manager.sku.SkuInfo;
import com.atguigu.gmall.search.constant.EsConstant;
import io.searchbox.client.JestClient;
import io.searchbox.core.*;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/8/6 0006
 */
@Slf4j
@Service
public class SkuEsServiceImpl implements SkuEsService {

    @Reference
    private SkuService skuService;
    @Autowired
    private JestClient jestClient;

    @Async//表示这是一个异步请求
    @Override
    public void onSale(Integer skuId) {
        try {
            //查出skuId对应的sku具体信息
            SkuInfo info = skuService.getSkuInfoBySkuId(skuId);
            SkuInfoEsVo skuInfoEsVo = new SkuInfoEsVo();
            //将内容复制给我们封装的JavaBean
            BeanUtils.copyProperties(info, skuInfoEsVo);
            //查出当前sku对应的所有的平台属性的值的id的集合并复制给skuInfoEsVo
            List<SkuBaseAttrEsVo> vos = skuService.getSkuBaseAttrValueIds(skuId);
            skuInfoEsVo.setBaseAttrEsVos(vos);
            //保存sku信息到es
            Index index = new Index.Builder(skuInfoEsVo).index(EsConstant.GMALL_INDEX)
                    .type(EsConstant.GMALL_SKU_TYPE).id(skuInfoEsVo.getId()+"").build();
            try {
                jestClient.execute(index);
            } catch (IOException e) {
                log.error("es保存数据出了问题，原因：{}",e);
            }
//            String str = JSON.toJSONString(info);
//            log.info("得到的数据为：{}",str);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从ES中查询出商品搜索页面展示的内容
     * @param paramEsVo
     * @return
     */
    @Override
    public SkuSearchResultEsVo searchSkuFromES(SkuSearchParamEsVo paramEsVo) {
        SkuSearchResultEsVo resultEsVo = null;
        //获取到所需的DSL语句
        String queryDsl = buildSkuSearchQueryDSL(paramEsVo);
        //构建Search对象
        Search search = new Search.Builder(queryDsl)
                .addIndex(EsConstant.GMALL_INDEX)
                .addType(EsConstant.GMALL_SKU_TYPE).build();
        try {
            //执行
            SearchResult result = jestClient.execute(search);
            //把查出出来的result处理成能给页面返回的SkuSearchResultEsVo对象
            resultEsVo = buildSkuSearchResult(result);
            resultEsVo.setPageNo(paramEsVo.getPageNo());
        } catch (IOException e) {
            log.error("ES查询出错，原因：{}",e);
        }
        return resultEsVo;
    }

    @Override
    public void updateHotScore(Integer skuId, Long hincrBy) {
        String updateHotScore = "{\"doc\":{\"hotScore\":"+hincrBy+"}}";
        Update update = new Update.Builder(updateHotScore)
                .index(EsConstant.GMALL_INDEX)
                .type(EsConstant.GMALL_SKU_TYPE)
                .id(skuId + "")
                .build();
        try {
            jestClient.execute(update);
        } catch (IOException e) {
            log.error("ES更新热度出问题了：{}",e);
        }
    }

    /**
     * 构建DSL语句（比较着kibana页面写好的dsl语句，一点一点拼接，缺少什么对象，就创建xxBuilder即可）
     * @param paramEsVo
     * @return
     */
    private String buildSkuSearchQueryDSL(SkuSearchParamEsVo paramEsVo) {
        //创建一个搜索数据用的构建器，里面可以拼接各种kibana中的操作
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //进行查询的构建（先通过QueryBuilders工具类拿到BoolQueryBuilder对象构造bool操作）
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            //过滤三级分类的信息
        if(paramEsVo.getCatalog3Id() != null && !paramEsVo.getCatalog3Id().equals("")) {
            TermQueryBuilder termCatalog3 = new TermQueryBuilder("catalog3Id",paramEsVo.getCatalog3Id());
            boolQuery.filter(termCatalog3);
        }
            //过滤用户所选择的平台属性值(可能选择多个平台属性的值，所以循环遍历)
        if(paramEsVo.getValueId() != null && paramEsVo.getValueId().length > 0) {
            for (Integer vid : paramEsVo.getValueId()) {
                TermQueryBuilder termValueId = new TermQueryBuilder("baseAttrEsVos.valueId",vid);
                boolQuery.filter(termValueId);
            }
        }
        //查询（根据前台输入的内容对skuName字段进行模糊匹配）
        if(!StringUtils.isEmpty(paramEsVo.getKeyword())) {
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName",paramEsVo.getKeyword());
            boolQuery.must(matchQueryBuilder);
            //高亮(高亮需要用到的时候则一定进行了模糊匹配，可以放在同一个判定中)
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.preTags("<span style='color:red'>");
            highlightBuilder.postTags("</span>");
            highlightBuilder.field("skuName");
            sourceBuilder.highlight(highlightBuilder);
        }
        sourceBuilder.query(boolQuery);

        //排序
        if(!StringUtils.isEmpty(paramEsVo.getSortField())) {
            SortOrder sortOrder = null;
            switch (paramEsVo.getSortOrder()) {
                case "desc":
                    sortOrder = SortOrder.DESC;
                    break;
                case "asc":
                    sortOrder = SortOrder.ASC;
                    break;
                default:
                    sortOrder = SortOrder.DESC;
            }
            sourceBuilder.sort("hotScore", sortOrder);
        }

        //分页
        sourceBuilder.from((paramEsVo.getPageNo()-1)*paramEsVo.getPageSize());
        sourceBuilder.size(paramEsVo.getPageSize());

        //聚合
        TermsBuilder termsBuilder = new TermsBuilder("valueIdAggs");
        termsBuilder.field("baseAttrEsVos.valueId");
        sourceBuilder.aggregation(termsBuilder);

        //toString就是获取到我们拼接的dsl语句
        String dsl = sourceBuilder.toString();
        return dsl;
    }

    /**
     * 将查出的结果构建为页面能用的vo对象数据
     * @param result
     * @return
     */
    private SkuSearchResultEsVo buildSkuSearchResult(SearchResult result) {
        //从es搜索到的结果中，找到所有的SkuInfo信息封装进SkuInfoEsVo中，然后再整体封装分页等信息进SkuSearchResultEsVo对象并返回
        SkuSearchResultEsVo resultEsVo = new SkuSearchResultEsVo();
        List<SkuInfoEsVo> skuInfoEsVoList =  null;
        //拿到命中的所有记录
        List<SearchResult.Hit<SkuInfoEsVo, Void>> hits = result.getHits(SkuInfoEsVo.class);
        if( hits == null || hits.size() == 0 ) {
            return null;
        } else {
            //查到了记录，讲这些记录遍历，然后每一个进行高亮操作封装后添加进集合中
            skuInfoEsVoList = new ArrayList<>(hits.size());
            for (SearchResult.Hit<SkuInfoEsVo, Void> hit : hits) {
                SkuInfoEsVo source = hit.source;//这里得到的source就是命中的每一个对象
                //查出的内容中可能有高亮，给高亮的内容也封装进对象中
                Map<String, List<String>> highlight = hit.highlight;
                if( highlight != null ) {
                    String higtText = highlight.get("skuName").get(0);
                    source.setSkuName(higtText);
                }
                skuInfoEsVoList.add(source);
            }
        }
        resultEsVo.setSkuInfoEsVos(skuInfoEsVoList);
        resultEsVo.setTotal(result.getTotal().intValue());
        resultEsVo.setBaseAttrInfos(getBaseAttrInfoGroupByValueId(result));

        return resultEsVo;
    }

    /**
     * 根据es中查询到的聚合的结果找到所有涉及到的平台属性对应的值
     * @param result
     * @return
     */
    private List<BaseAttrInfo> getBaseAttrInfoGroupByValueId(SearchResult result) {
        MetricAggregation aggregations = result.getAggregations();
        TermsAggregation valueIdAggs = aggregations.getTermsAggregation("valueIdAggs");
        List<TermsAggregation.Entry> buckets = valueIdAggs.getBuckets();
        List<Integer> valueIds = new ArrayList<>();
        for (TermsAggregation.Entry bucket : buckets) {
            String key = bucket.getKey();
            valueIds.add(Integer.parseInt(key));
        }
        return skuService.getBaseAttrInfoGroupByValueId(valueIds);
    }


}
