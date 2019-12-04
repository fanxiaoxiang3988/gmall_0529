package com.atguigu.rabbit.service;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/11/29 0029
 */
@Service
public class UserService {

    /**
     * 只要消息抵达消费者，消息就会从消息队列中自动移除
     * @param msg
     */
    @RabbitListener(queues = {"atguigu","atguigu.news"})
    public void hello(Message msg) {

        System.out.println("从RabbitMq传过来的消息是：" + msg);
        byte[] body = msg.getBody();//消息的内容
        System.out.println("从RabbitMq传过来的消息是：" + new String(body));
        MessageProperties messageProperties = msg.getMessageProperties();//消息的属性
        System.out.println(messageProperties);

    }

    /**
     * 可以写三种参数：
     * 1：Object object 可以直接用对象或者String来接收，直接接收消息的内容
     * 2: Message message 包含消息的内容以及各种属性
     * 3:Channel channel
     */
    @RabbitListener(queues = "gulixueyuan.news")
    public void order(Order order, Message message, Channel channel) throws IOException {
        /*System.out.println("收到的消息：" + order);
        System.out.println("收到的消息：" + message);*/
        //测试拒绝消息   long deliveryTag, boolean requeue
        AMQP.Basic.Reject reject = new AMQP.Basic.Reject.Builder().build();
        long deliveryTag = reject.getDeliveryTag();
        channel.basicReject(deliveryTag, true);
        System.out.println("系统成功拒绝了这条订单消息的推送：" + message);

    }

}
