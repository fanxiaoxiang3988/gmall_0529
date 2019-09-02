package com.atguigu.gmall.search;

import java.util.List;
import org.junit.Test;
import java.util.HashMap;
import java.io.IOException;
import io.searchbox.core.Search;
import org.junit.runner.RunWith;
import io.searchbox.client.JestClient;
import io.searchbox.core.SearchResult;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;



@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallSearchServiceApplicationTests {

    @Autowired
    private JestClient jestClient;


//    @Test
//    public void testES() throws IOException {
//        SkuEsServiceImpl esService = new SkuEsServiceImpl();
//        SkuSearchParamEsVo paramEsVo = new SkuSearchParamEsVo();
//        paramEsVo.setCatalog3Id(61);
//        paramEsVo.setKeyword("小米");
//        paramEsVo.setValueId(new Integer[]{82,83});
//        paramEsVo.setPageNo(1);
//        String s = esService.buildSkuSearchQueryDSL(paramEsVo);
//        System.out.println(s);
//
//        Search search = new Search.Builder(s)
//                .addIndex(EsConstant.GMALL_INDEX)
//                .addType(EsConstant.GMALL_SKU_TYPE).build();
//
//        SearchResult result = jestClient.execute(search);
//        System.out.println("啦啦啦啦啦啦啦啦啦啦啦"+result);
//        List<SearchResult.Hit<SkuInfoEsVo, Void>> hits = result.getHits(SkuInfoEsVo.class);
//        System.out.println(hits);
//        for (SearchResult.Hit<SkuInfoEsVo, Void> hit : hits) {
//            System.out.println("+++++++++++++" + hit.source.getSkuName());
//            System.out.println(hit.source);
//            System.out.println(hit.highlight);
//            System.out.println(result.getTotal());
//        }
//    }

    @Test
    public void contextLoads() throws IOException {
        //https://www.elastic.co/guide/en/elasticsearch/reference/current/_exploring_your_data.html
        //可以导入这个测试库里面有很多数据
        /**
         * 1、先去上面的网址下载来测试数据，并保存为accounts.json
         * 2、进入accounts.json所在目录运行如下指令即可
         * curl -H "Content-Type: application/json" -XPOST "192.168.216.128:9200/bank/doc/_bulk?pretty&refresh" --data-binary "@accounts.json"
         */

        String query="{\n" +
                "  \"query\": {\n" +
                "    \"match\": {\n" +
                "      \"actorList.name\": \"张译\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        //GET /movie_chn/movie/_search
        Search search = new Search.Builder(query).addIndex("movie_chn").addType("movie").build();
        SearchResult result  = jestClient.execute(search);
        List<SearchResult.Hit<HashMap, Void>> hits = result.getHits(HashMap.class);
        for (SearchResult.Hit<HashMap, Void> hit : hits) {
            System.out.println(hit.score);
            System.out.println(hit.source);
        }
        System.out.println(result + "==================================");
    }

}
