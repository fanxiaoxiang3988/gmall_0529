package com.atguigu.gmall.order;

import lombok.Data;
import java.io.Serializable;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/11/20 0020
 */
@Data
public class OrderInfoTo implements Serializable {

    private String orderComment;//订单的备注

    private String consignee; //收货人

    private String consigneeTel; //电话

    private String deliveryAddress; //收货地址

}
