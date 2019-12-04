package com.atguigu.rabbit.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/12/2 0002
 */
@Configuration
public class GmallRabbitConfig {

    /**
     * SpringBoot的新特性：
     * @Configuration和@Bean配合，注入容器中，类似于改变xml文件
     * 只要将Queue,Exchange,Binging放在容器中，再启动项目的时候，都会自动创建这些队列内容
     * 如果RabbitMq已经创建过，就不会再次创建了
     */

    //创建一个用户订单的延时队列
    @Bean
    Queue userOrderDelayQueue() {
        /**
         * String name
         * boolean durable 是否持久化
         * boolean exclusive 是否排它，一般false
         * boolean autoDelete 是否自动删除
         * Map<String, Object> arguments
         */
        //给队列指定延迟规则
        Map<String,Object> args = new HashMap<>();
        //1、这个队列的里面的信死了去哪里？ 去哪个DLX（Dead Letter Exchange）
        args.put("x-dead-letter-exchange","user.order.exchange");
        //2、死了以后用哪个路由键发出去
        args.put("x-dead-letter-routing-key","order");

        Queue queue = new Queue("user_order_delay_queue", true, false, false, args);
        return queue;
    }

    //创建一个延时交换机
    @Bean
    Exchange userOrderDelayExchange() {
        DirectExchange exchange = new DirectExchange("user_order_delay_exchange", true, false, null);
        return exchange;
    }

    //将延时交换机和延时队列绑定
    @Bean
    Binding userOrderDelayExchangeQueueBinding() {
        Binding order_delay = BindingBuilder.bind(userOrderDelayQueue())
                .to(userOrderDelayExchange())
                .with("order_delay").noargs();
        return order_delay;
    }

    //========================以上创建了一个延迟队列和绑定关系==================================

    //=====================创建接受死信的Exchange（DLX）==============
    @Bean
    Exchange userOrderExchange() {
        Exchange directExchange = new DirectExchange("user.order.exchange");
        return directExchange;
    }
    //创建一个存死信的队列
    @Bean
    Queue userOrderQueue() {
        Queue queue = new Queue("user.order.queue");
        return queue;
    }
    //创建绑定关系
    @Bean
    Binding userOrderExchangeQueueBinding() {
        Binding order = BindingBuilder.bind(userOrderQueue()).to(userOrderExchange()).with("order").noargs();
        return order;
    }

    /**
     * 标配
     * 定制了消息的转换规则，以json的格式发送
     * @return
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


}
