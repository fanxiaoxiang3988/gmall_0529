package com.atguigu.gmall.pay;

import com.atguigu.gmall.SuperBean;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/11/27 0027
 */
@Data
public class PaymentInfo extends SuperBean {

    private String outTradeNo;
    private Integer orderId;
    private BigDecimal totalAmount;
    private String subject;//订单的描述
    private String paymentStatus; //TRADE_SUCCESS TRADE_FINISHED
    private Date createTime;

    private String alipayTradeNo;//支付宝流水号
    private Date callbackTime;//支付宝回调时间
    private String callbackContent;//支付宝回调内容

}
