package com.atguigu.rabbit.controller;

import com.atguigu.rabbit.service.Order;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/12/9 0009
 */
@Api("测试功能")
@RestController
public class HelloController {

    @ApiOperation("testCreateOrder")
    @GetMapping("/testCreateOrder")
    public Order OrderCreateOrder(Order o) {
        Order order = new Order();
        order.setOrderId(o.getOrderId()+"_new");
        order.setStatus(o.getStatus());
        order.setTotalAmount(o.getTotalAmount());
        return order;
    }

}
