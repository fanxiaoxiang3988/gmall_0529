package com.atguigu.gmall.order;

import com.atguigu.gmall.cart.CartItem;
import com.atguigu.gmall.user.UserAddress;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单提交需要的数据（购物车结算页封装实体类）
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/11/14 0014
 */
@Data
public class TradePageVo implements Serializable {

    private List<UserAddress> userAddresses;//用户收货列表

    private List<CartItem> cartItems;//商品清单

    private BigDecimal totalPrice;//商品总价

}
