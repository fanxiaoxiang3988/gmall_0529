package com.atguigu.gmall.order;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/11/18 0018
 */
public interface OrderService {

    String createTradeToken();

    boolean verfyToken(String tradeToken);
}
