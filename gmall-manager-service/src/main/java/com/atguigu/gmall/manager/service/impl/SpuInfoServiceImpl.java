package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.manager.BaseAttrInfo;
import com.atguigu.gmall.manager.SpuInfoService;
import com.atguigu.gmall.manager.mapper.spu.*;
import com.atguigu.gmall.manager.spu.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/19 0019
 */
@Slf4j
@Service
public class SpuInfoServiceImpl implements SpuInfoService {

    @Autowired
    private SpuInfoMapper spuInfoMapper;
    @Autowired
    private BaseSaleAttrMapper baseSaleAttrMapper;
    @Autowired
    private SpuImageMapper spuImageMapper;
    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;
    @Autowired
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;


    @Override
    public List<SpuInfo> getSpuInfoByC3Id(Integer catalog3Id) {
        QueryWrapper<SpuInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("catalog3_id",catalog3Id);
        List<SpuInfo> spuInfos = spuInfoMapper.selectList(wrapper);
        return spuInfos;
    }

    @Override
    public List<BaseSaleAttr> getBaseSaleAttr() {
        return baseSaleAttrMapper.selectList(null);
    }

    @Transactional
    @Override
    public void saveBigSpuInfo(SpuInfo spuInfo) {
        //1、保存spu的基本信息
        spuInfoMapper.insert(spuInfo);
        //获取到刚才保存的spu的id
        Integer spuId = spuInfo.getId();
        //2、保存spu的图片信息
        List<SpuImage> spuImages = spuInfo.getSpuImages();
        for (SpuImage spuImage : spuImages) {
            //获取到刚刚插入数据的spu的id
            spuImage.setSpuId(spuId);
            spuImageMapper.insert(spuImage);
        }
        //3、保存spu的销售属性信息
        List<SpuSaleAttr> spuSaleAttrs = spuInfo.getSpuSaleAttrs();
        for (SpuSaleAttr spuSaleAttr : spuSaleAttrs) {
            spuSaleAttr.setSpuId(spuId);
            spuSaleAttrMapper.insert(spuSaleAttr);
            //4、保存spu的销售属性值信息
            List<SpuSaleAttrValue> saleAttrValues = spuSaleAttr.getSaleAttrValues();
            for (SpuSaleAttrValue saleAttrValue : saleAttrValues) {
                saleAttrValue.setSpuId(spuId);
                saleAttrValue.setSaleAttrId(spuSaleAttr.getSaleAttrId());
                spuSaleAttrValueMapper.insert(saleAttrValue);
            }
        }
    }



}
