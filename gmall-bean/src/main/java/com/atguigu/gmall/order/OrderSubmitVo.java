package com.atguigu.gmall.order;

import lombok.Data;
import java.io.Serializable;

/**
 * 用户点击提交订单时，需要提交的数据
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/11/18 0018
 */
@Data
public class OrderSubmitVo implements Serializable {

    private Integer userAddressId;//用户地址的id

    private String orderComment;//订单的备注

    private String tradeToken;//防重复提交令牌

}
