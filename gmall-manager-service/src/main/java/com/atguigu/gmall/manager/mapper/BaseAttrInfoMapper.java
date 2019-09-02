package com.atguigu.gmall.manager.mapper;

import com.atguigu.gmall.manager.BaseAttrInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/16 0016
 */
public interface BaseAttrInfoMapper extends BaseMapper<BaseAttrInfo> {

    int insertNewInfo(BaseAttrInfo info);

    List<BaseAttrInfo> getBaseAttrInfoByCatalog3Id(@Param("catalog3Id") Integer catalog3Id);

    List<BaseAttrInfo> getBaseAttrInfoGroupByValueId(@Param("valueIds") List<Integer> valueIds);
}
