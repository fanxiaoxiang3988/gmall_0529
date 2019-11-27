package com.atguigu.gmall.order;

import com.atguigu.gmall.user.UserAddress;

import java.io.IOException;
import java.util.List;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/11/18 0018
 */
public interface OrderService {

    String createTradeToken();

    boolean verfyToken(String tradeToken);

    List<String> verfyStock(int userId) throws IOException;

    UserAddress getUserAddressById(Integer userAddressId);

    OrderInfo createOrder(int userId, OrderInfoTo orderInfoTo);

    OrderInfo getOrderById(Integer id);

    void updateOrderPaySuccess(String out_trade_no);
}
