package com.atguigu.gmall.manager;

import com.atguigu.gmall.manager.es.SkuBaseAttrEsVo;
import com.atguigu.gmall.manager.sku.SkuAttrValueMappingTo;
import com.atguigu.gmall.manager.sku.SkuInfo;
import com.atguigu.gmall.manager.spu.SpuSaleAttr;

import java.util.List;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/24 0024
 */
public interface SkuService {

    List<BaseAttrInfo> getBaseAttrInfoByCatalog3Id(Integer catalog3Id);

    List<SpuSaleAttr> getSpuSaleAttrBySpuId(Integer spuId);

    void saveBigSkuInfo(SkuInfo skuInfo);

    List<SkuInfo> getSkuInfoBySpuId(Integer spuId);

    SkuInfo getSkuInfoBySkuId(Integer skuId) throws InterruptedException;

    List<SkuAttrValueMappingTo> getSkuAttrValueMapping(Integer spuId);

    List<SkuBaseAttrEsVo> getSkuBaseAttrValueIds(Integer skuId);
}
