package com.atguigu.gmall.manager.es;

import lombok.Data;
import java.io.Serializable;

/**
 * Sku平台属性在es中保存的信息
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/8/7 0007
 */
@Data
public class SkuBaseAttrEsVo implements Serializable {

    private Integer valueId;//平台属性值的id

}
