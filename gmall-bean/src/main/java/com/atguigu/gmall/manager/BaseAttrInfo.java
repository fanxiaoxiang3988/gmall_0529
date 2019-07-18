package com.atguigu.gmall.manager;

import com.atguigu.gmall.SuperBean;
import lombok.Data;

/**
 * 平台属性名表
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/16 0016
 */
@Data
public class BaseAttrInfo extends SuperBean {

    private String attrName;

    private Integer catalog3Id;
}
