package com.atguigu.gmall.manager;

import com.atguigu.gmall.manager.es.SkuSearchParamEsVo;
import com.atguigu.gmall.manager.es.SkuSearchResultEsVo;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/8/6 0006
 */
public interface SkuEsService {

    //商品的上架
    void onSale(Integer skuId);

    SkuSearchResultEsVo searchSkuFromES(SkuSearchParamEsVo paramEsVo);

    void updateHotScore(Integer skuId, Long hincrBy);
}
