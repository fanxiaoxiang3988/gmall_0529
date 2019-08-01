package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.manager.BaseAttrInfo;
import com.atguigu.gmall.manager.SkuService;
import com.atguigu.gmall.manager.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.manager.mapper.sku.SkuAttrValueMapper;
import com.atguigu.gmall.manager.mapper.sku.SkuImageMapper;
import com.atguigu.gmall.manager.mapper.sku.SkuInfoMapper;
import com.atguigu.gmall.manager.mapper.sku.SkuSaleAttrValueMapper;
import com.atguigu.gmall.manager.mapper.spu.SpuSaleAttrMapper;
import com.atguigu.gmall.manager.sku.*;
import com.atguigu.gmall.manager.spu.SpuSaleAttr;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/24 0024
 */
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;
    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;
    @Autowired
    private SkuInfoMapper skuInfoMapper;
    @Autowired
    private SkuImageMapper skuImageMapper;
    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;
    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Override
    public List<BaseAttrInfo> getBaseAttrInfoByCatalog3Id(Integer catalog3Id) {
        return baseAttrInfoMapper.getBaseAttrInfoByCatalog3Id(catalog3Id);
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrBySpuId(Integer spuId) {
        return spuSaleAttrMapper.getSpuSaleAttrBySpuId(spuId);
    }

    @Transactional
    @Override
    public void saveBigSkuInfo(SkuInfo skuInfo) {
        //保存skuInfo的基本信息
        skuInfoMapper.insert(skuInfo);
        Integer id = skuInfo.getId();
        //保存skuImages
        List<SkuImage> skuImages = skuInfo.getSkuImages();
        for (SkuImage skuImage : skuImages) {
            skuImage.setSkuId(id);
            skuImageMapper.insert(skuImage);
        }
        //保存skuAttrValues
        List<SkuAttrValue> skuAttrValues = skuInfo.getSkuAttrValues();
        for (SkuAttrValue skuAttrValue : skuAttrValues) {
            skuAttrValue.setSkuId(skuInfo.getId());
            skuAttrValueMapper.insert(skuAttrValue);
        }
        //保存skuSaleAttrValues
        List<SkuSaleAttrValue> skuSaleAttrValues = skuInfo.getSkuSaleAttrValues();
        for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValues) {
            skuSaleAttrValue.setSkuId(skuInfo.getId());
            skuSaleAttrValueMapper.insert(skuSaleAttrValue);
        }
    }

    @Override
    public List<SkuInfo> getSkuInfoBySpuId(Integer spuId) {
        QueryWrapper<SkuInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("spu_id",spuId);
        return skuInfoMapper.selectList(wrapper);
    }

    @Transactional
    @Override
    public SkuInfo getSkuInfoBySkuId(Integer skuId) {
        //查出这个sku的所有基本信息
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        //查出这个sku的所有图片信息
        QueryWrapper<SkuImage> wrapper = new QueryWrapper<>();
        wrapper.eq("sku_id",skuInfo.getId());
        List<SkuImage> skuImages = skuImageMapper.selectList(wrapper);
        skuInfo.setSkuImages(skuImages);
        //查出sku所有的销售属性及其对应的所有值封装进属性：private List<SkuAllSaveAttrAndValueTo> skuAllSaveAttrAndValueTos
        List<SkuAllSaveAttrAndValueTo> skuAllSaveAttrAndValueTos = skuImageMapper.getSkuAllSaveAttrAndValue(skuInfo.getId(),skuInfo.getSpuId());
        skuInfo.setSkuAllSaveAttrAndValueTos(skuAllSaveAttrAndValueTos);

        return skuInfo;
    }

    @Override
    public List<SkuAttrValueMappingTo> getSkuAttrValueMapping(Integer spuId) {
        return skuInfoMapper.getSkuAttrValueMapping(spuId);
    }
}
