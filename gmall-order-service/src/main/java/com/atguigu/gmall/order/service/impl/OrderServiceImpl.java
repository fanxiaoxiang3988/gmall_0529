package com.atguigu.gmall.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/11/18 0018
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private JedisPool jedisPool;


    /**
     * 创建一个令牌，存储在redis中，用于和页面的进行比较
     * @return
     */
    @Override
    public String createTradeToken() {

        String token = UUID.randomUUID().toString().replaceAll("-", "");
        Jedis jedis = jedisPool.getResource();
        jedis.setex(token, 60*5, "fanxiaoxiang");
        jedis.close();
        return token;
    }

    /**
     * 验证令牌，由于令牌验证后也需要删除，可以直接用redis的删除方法，删除成功返回1，失败返回0
     * @param tradeToken
     * @return
     */
    @Override
    public boolean verfyToken(String tradeToken) {
        Jedis jedis = jedisPool.getResource();
        Long del = jedis.del(tradeToken);
        jedis.close();
        return del==1?true:false;
    }

    /**
     * 验证库存是否充足
     * @param userId 当前登录用户的id
     * @return 所有库存不足的商品的商品id
     */
    @Override
    public List<String> verfyStock(int userId) {
        return null;
    }

}
