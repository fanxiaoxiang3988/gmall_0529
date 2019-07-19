package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.manager.BaseAttrInfo;
import com.atguigu.gmall.manager.BaseAttrInfoService;
import com.atguigu.gmall.manager.BaseAttrValue;
import com.atguigu.gmall.manager.BaseCatalog3;
import com.atguigu.gmall.manager.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.manager.mapper.BaseAttrValueMapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/16 0016
 */
@Slf4j
@Service
public class BaseAttrInfoServiceImpl implements BaseAttrInfoService {

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;
    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;

    @Override
    public List<BaseAttrInfo> getBaseAttrInfoByCatalog3Id(Integer catalog3Id) {
        QueryWrapper<BaseAttrInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("catalog3_id",catalog3Id);
        List<BaseAttrInfo> baseAttrInfos = baseAttrInfoMapper.selectList(wrapper);
        return baseAttrInfos;
    }

    @Override
    public List<BaseAttrValue> getBaseAttrValueByAttrId(Integer baseAttrInfoId) {
        QueryWrapper<BaseAttrValue> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_id",baseAttrInfoId);
        List<BaseAttrValue> baseAttrValues = baseAttrValueMapper.selectList(wrapper);
        return baseAttrValues;
    }

    @Transactional
    @Override
    public void saveOrUpdateBaseInfo(BaseAttrInfo baseAttrInfo) {
        //log.info("准备修改的BaseAttrInfo信息是：{},没有id？{}",baseAttrInfo,baseAttrInfo.getId());
        //根据传来的baseAttrInfo判断是修改还是添加
        if(baseAttrInfo.getId() != null) {
            //修改基本属性名
            baseAttrInfoMapper.updateById(baseAttrInfo);
            //删除数据库有而此次未提交的
            List<BaseAttrValue> attrValues = baseAttrInfo.getAttrValues();
            List<Integer> ids = new ArrayList<>();
            for (BaseAttrValue attrValue : attrValues) {
                //查询到提交的集合，只要是没有提交的，均是删除的
                Integer id = attrValue.getId();
                if(id!=null){
                    ids.add(id);
                }
            }
            //根据ids查询出attr_id相等的id不在ids里面的所有的平台属性值，然后进行删除
            QueryWrapper<BaseAttrValue> wrapper = new QueryWrapper<>();
            wrapper.eq("attr_id",baseAttrInfo.getId());
            wrapper.notIn("id",ids);
            baseAttrValueMapper.delete(wrapper);
            for (BaseAttrValue attrValue : attrValues) {
                if(attrValue.getId() != null) {
                    //提交过来的数据，如果有id就是修改
                    baseAttrValueMapper.updateById(attrValue);

                } else {
                    //没有id就是新增
                    attrValue.setAttrId(baseAttrInfo.getId());
                    baseAttrValueMapper.insert(attrValue);
                }
            }
        } else {
            //添加
            BaseAttrInfo info = new BaseAttrInfo();
            info.setAttrName(baseAttrInfo.getAttrName());
            info.setCatalog3Id(baseAttrInfo.getCatalog3Id());
            //添加平台属性
            baseAttrInfoMapper.insertNewInfo(info);
            //log.info("数据插入成功，插入的平台属性，其id是：{}",info.getId());
            //添加平台属性值
            List<BaseAttrValue> attrValues = baseAttrInfo.getAttrValues();
            for (BaseAttrValue attrValue : attrValues) {
                if(info.getId() != null) {
                    attrValue.setAttrId(info.getId());
                    baseAttrValueMapper.insert(attrValue);
                }
            }
        }
    }

    @Override
    public int deleteAttrInfoById(Integer id) {

        return baseAttrInfoMapper.deleteById(id);
    }

    @Override
    public int deleteAttrValueInfoById(Integer id) {
        QueryWrapper<BaseAttrValue> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_id", id);
        return baseAttrValueMapper.delete(wrapper);
    }

}
