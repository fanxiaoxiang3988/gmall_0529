package com.atguigu.gmall.pay;

import lombok.Data;
import java.io.Serializable;

/**
 * 封装阿里生成的订单页面的四个基本信息
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/11/26 0026
 */
@Data
public class AlipayRequestVo implements Serializable {

    private String outTradeNo;//订单号
    private String subject;//订单名字
    private String totalAmount;//订单需要支付的总额
    private String body;//订单的描述

    private String productCode = "FAST_INSTANT_TRADE_PAY";

}
