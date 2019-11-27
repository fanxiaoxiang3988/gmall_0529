package com.atguigu.gmall.pay;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/11/27 0027
 */
public interface PaymentService {
    void createAliTrade(PaymentInfo paymentInfo);

    void updatePayement(PaymentInfo paymentInfo);
}
