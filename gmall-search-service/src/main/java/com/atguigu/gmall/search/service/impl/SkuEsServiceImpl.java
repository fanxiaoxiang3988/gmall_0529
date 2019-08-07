package com.atguigu.gmall.search.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.manager.SkuEsService;
import com.atguigu.gmall.manager.SkuService;
import com.atguigu.gmall.manager.es.SkuBaseAttrEsVo;
import com.atguigu.gmall.manager.es.SkuInfoEsVo;
import com.atguigu.gmall.manager.sku.SkuInfo;
import com.atguigu.gmall.search.constant.EsConstant;
import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.queryparser.flexible.core.parser.EscapeQuerySyntax;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.util.List;

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



}
