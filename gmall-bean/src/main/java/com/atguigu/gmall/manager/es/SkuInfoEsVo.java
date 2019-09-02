package com.atguigu.gmall.manager.es;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Es中保存的用于search.gmall.com/list.html页面展示的skuInfo的信息
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/8/7 0007
 */
@Data
public class SkuInfoEsVo implements Serializable {

    private Integer id;//sku的id

    private BigDecimal price; //sku的价格

    private String skuName; //sku的名字   //全文检索，分词

    private String skuDesc; //sku的描述

    private Integer catalog3Id; //sku的3级分类id

    private String skuDefaultImg; //sku的默认图片地址

    private Long hotScore=0L; //热度评分

    //在es中使用平台属性值的id可以进行过滤
    private List<SkuBaseAttrEsVo> baseAttrEsVos; //把sku的平台属性的值保存过来，只需要存平台属性值的id

}
