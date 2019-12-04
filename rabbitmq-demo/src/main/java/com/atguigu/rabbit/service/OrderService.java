package com.atguigu.rabbit.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/12/4 0004
 */
@Service
public class OrderService {

    @RabbitListener(queues = "user.order.queue")
    public void receiveOrder(Order order) {
        System.out.println("收到了过期订单。。。"+order);
        //查讯最新的订单状态
        if(order.getStatus()%2==0){
            System.out.println("此订单已经支付成功...告诉库存系统去减库存");
        } else {
            System.out.println("此订单已经超时了，而且还没支付，，，作废。。。");
        }
    }



}
