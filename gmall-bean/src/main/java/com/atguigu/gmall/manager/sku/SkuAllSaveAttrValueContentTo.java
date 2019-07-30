package com.atguigu.gmall.manager.sku;

import lombok.Data;
import java.io.Serializable;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/30 0030
 */
@Data
public class SkuAllSaveAttrValueContentTo implements Serializable {

    private Integer saleAttrValueId;
    private String  saleAttrValueName;
    private Integer skuId;
    private Integer isCheck;

}
