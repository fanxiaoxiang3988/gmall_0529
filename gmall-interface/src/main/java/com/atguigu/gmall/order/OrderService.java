package com.atguigu.gmall.order;

import java.util.List;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/11/18 0018
 */
public interface OrderService {

    String createTradeToken();

    boolean verfyToken(String tradeToken);

    List<String> verfyStock(int userId);
}
