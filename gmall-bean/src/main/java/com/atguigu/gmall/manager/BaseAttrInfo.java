package com.atguigu.gmall.manager;

import com.atguigu.gmall.SuperBean;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.util.List;

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

    @TableField(exist = false)  //数据库并不存在此字段
    private List<BaseAttrValue> attrValues;

}
