package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.manager.BaseCatalog1;
import com.atguigu.gmall.manager.BaseCatalog2;
import com.atguigu.gmall.manager.BaseCatalog3;
import com.atguigu.gmall.manager.CatalogService;
import com.atguigu.gmall.manager.mapper.BaseCatalog1Mapper;
import com.atguigu.gmall.manager.mapper.BaseCatalog2Mapper;
import com.atguigu.gmall.manager.mapper.BaseCatalog3Mapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/16 0016
 */
@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private BaseCatalog1Mapper baseCatalog1Mapper;
    @Autowired
    private BaseCatalog2Mapper baseCatalog2Mapper;
    @Autowired
    private BaseCatalog3Mapper baseCatalog3Mapper;

    @Override
    public List<BaseCatalog1> getAllBaseCatalog1() {
        List<BaseCatalog1> baseCatalog1s = baseCatalog1Mapper.selectList(null);
        return baseCatalog1s;
    }

    @Override
    public List<BaseCatalog2> getBaseCatalog2ByC1id(Integer catalog1Id) {
        QueryWrapper<BaseCatalog2> wrapper = new QueryWrapper<>();
        wrapper.eq("catalog1_id",catalog1Id);
        List<BaseCatalog2> baseCatalog2s = baseCatalog2Mapper.selectList(wrapper);
        return baseCatalog2s;
    }

    @Override
    public List<BaseCatalog3> getBaseCatalog3ByC2id(Integer catalog2Id) {
        QueryWrapper<BaseCatalog3> wrapper = new QueryWrapper<>();
        wrapper.eq("catalog2_id", catalog2Id);
        List<BaseCatalog3> baseCatalog3s = baseCatalog3Mapper.selectList(wrapper);
        return baseCatalog3s;
    }
}
