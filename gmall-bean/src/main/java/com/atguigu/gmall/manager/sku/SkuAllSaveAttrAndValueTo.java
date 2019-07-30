package com.atguigu.gmall.manager.sku;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/30 0030
 */
@Data
public class SkuAllSaveAttrAndValueTo implements Serializable {

    private Integer id;
    private Integer spuId;
    private Integer saleAttrId;
    private String saleAttrName;
//    private Integer saleAttrValueId;
//    private String saleAttrValueName;
//    private Integer skuId;
//    private Integer isCheck;
    // sale_attr_value_id  sale_attr_value_name   sku_id  is_check
    private List<SkuAllSaveAttrValueContentTo> valueContent;

}
