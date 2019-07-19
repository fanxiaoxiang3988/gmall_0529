package com.atguigu.gmall.manager.vo;

import lombok.Data;
import java.util.List;

/**
 * Vo类，用来映射web端传来的数据(收集页面的值封装为指定对象)
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/18 0018
 */
@Data
public class BaseAttrInfoAndValueVO {

    private Integer id;

    private String attrName;

    private Integer catalog3Id;

    private List<BaseAttrValueVo> attrValues;

}
