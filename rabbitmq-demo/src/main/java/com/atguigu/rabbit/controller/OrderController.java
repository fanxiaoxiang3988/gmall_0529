package com.atguigu.rabbit.controller;

import com.atguigu.rabbit.service.Order;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/12/3 0003
 */
@RestController
public class OrderController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/createOrder")
    public Order createOrder() {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString().substring(0,5));
        double v = Math.random() * 100;
        long round = Math.round(v);
        int i = Integer.parseInt(round + "");
        order.setStatus(i);
        order.setTotalAmount(new BigDecimal("99.88"));
        //给延时队列发送消息，告诉队列订单创建完成，并且队列的消息是有时间限制的
        //MessagePostProcessor 消息对象创建好之后，发之前可以设置消息的一些属性
        rabbitTemplate.convertAndSend("user_order_delay_exchange", "order_delay", order,
                new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        message.getMessageProperties().setExpiration(1000*60+"");//设置过期时间，1分钟
                        return message;
                    }
                });
        return order;
    }

}
