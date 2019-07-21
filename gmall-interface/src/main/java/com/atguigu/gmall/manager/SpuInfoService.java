package com.atguigu.gmall.manager;

import com.atguigu.gmall.manager.spu.SpuInfo;

import java.util.List;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/19 0019
 */
public interface SpuInfoService {
    List<SpuInfo> getSpuInfoByC3Id(Integer catalog3Id);
}
