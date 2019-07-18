package com.atguigu.gmall.manager;

import com.atguigu.gmall.SuperBean;
import lombok.Data;

/**
 * 三级分类表
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/16 0016
 */
@Data
public class BaseCatalog3 extends SuperBean {

    private String name;

    private Integer catalog2Id;

}
