package com.atguigu.rabbit;

import com.atguigu.rabbit.service.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqDemoApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;//负责收发消息

    @Autowired
    private AmqpAdmin amqpAdmin;//CRUD各种交换机，队列，绑定关系（主要负责管理消息队列）

    //测试消息队列的发送
    @Test
    public void contextLoads() {

        //rabbitTemplate.convertAndSend("gmall.exchange.direct", "atguigu.news", "今天天气很好。。。");
        Order order = new Order();
        order.setOrderId("1");
        //order.setStatus("UPPAID");
        order.setTotalAmount(new BigDecimal("999.98"));
        rabbitTemplate.convertAndSend("gmall.exchange.direct", "gulixueyuan.news", order);

    }

    //测试交换机、队列的创建，以及交换机与队列之间关系的绑定
    @Test
    public void createQueue() {

        //创建队列
        /*Queue queue = new Queue("hello-queue");
        amqpAdmin.declareQueue(queue);
        System.out.println("队列创建成功。。。。");*/
        //创建交换机
        /*DirectExchange exchange = new DirectExchange("hello-directExchange");
        amqpAdmin.declareExchange(exchange);
        System.out.println("交换机创建成功。。。。");*/
        //创建绑定规则
        /**
         * String destination,目的地（填写队列名称）
         * DestinationType destinationType,（目的地类型，队列或交换机，用枚举类型表示）
         * String exchange,（交换机名称）
         * String routingKey,（路由键）
         * Map<String, Object> arguments（其他参数）
         */
        Binding binding = new Binding("hello-queue", Binding.DestinationType.QUEUE, "hello-directExchange","hello.msg",null);
        amqpAdmin.declareBinding(binding);
        System.out.println("交换机与队列绑定成功。。。。");

    }

}
