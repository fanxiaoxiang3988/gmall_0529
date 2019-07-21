package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.manager.SpuInfoService;
import com.atguigu.gmall.manager.mapper.spu.SpuInfoMapper;
import com.atguigu.gmall.manager.spu.SpuInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

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


    @Override
    public List<SpuInfo> getSpuInfoByC3Id(Integer catalog3Id) {
        QueryWrapper<SpuInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("catalog3_id",catalog3Id);
        List<SpuInfo> spuInfos = spuInfoMapper.selectList(wrapper);
        return spuInfos;
    }
}
