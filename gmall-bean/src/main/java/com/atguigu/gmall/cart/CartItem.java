package com.atguigu.gmall.cart;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 购物车的每一项数据
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/9/23 0023
 */
@Setter
public class CartItem implements Serializable {

    @Getter
    private SkuItem skuItem;//当前这一个购物项商品的详情

    @Getter
    private Integer num;//当前项数量

    private BigDecimal totalPrice;//当前项的总价

    @Getter
    @Setter
    private boolean isCheck = false;//可以发ajax请求更新此字段

    /**
     * 自动计算这一项商品的总价
     * @return
     */
    public BigDecimal getTotalPrice() {
        BigDecimal decimal = skuItem.getPrice().multiply(new BigDecimal(num));
        return decimal;
    }



}
