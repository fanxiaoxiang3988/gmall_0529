package com.atguigu.gmall.search;

import com.atguigu.gmall.manager.es.SkuSearchParamEsVo;
import com.atguigu.gmall.search.constant.EsConstant;
import com.atguigu.gmall.search.service.impl.SkuEsServiceImpl;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.Test;

import java.util.LinkedList;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/8/13 0013
 */
public class SimpleTest {

    @Test
    public void testDSL() {

        LinkedList<Object> objects = new LinkedList<>();
        objects.add(1);
        objects.add(2);
        objects.add(3);
        objects.add(4);
        objects.add(5);
        objects.add(6);
        int size = objects.size();
        System.out.println(size);
        System.out.println("=========="+objects.get(3));

        /*SkuEsServiceImpl esService = new SkuEsServiceImpl();
        SkuSearchParamEsVo paramEsVo = new SkuSearchParamEsVo();
        paramEsVo.setCatalog3Id(61);
        paramEsVo.setKeyword("小米");
        paramEsVo.setValueId(new Integer[]{82,83});
        paramEsVo.setPageNo(1);
        String s = esService.buildSkuSearchQueryDSL(paramEsVo);
        System.out.println(s);

        Search search = new Search.Builder(s)
                .addIndex(EsConstant.GMALL_INDEX)
                .addType(EsConstant.GMALL_SKU_TYPE).build();

        SearchResult result = jestClient.execute(search);*/

    }

}
