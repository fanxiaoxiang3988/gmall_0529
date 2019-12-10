package com.atguigu.rabbit.service;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/11/29 0029
 */
@Data
public class Order implements Serializable {

    private String orderId;//订单号
    private Integer status;//订单状态
    private BigDecimal totalAmount;//订单的金额

}
