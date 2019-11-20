package com.atguigu.gmall.order.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.cart.CartItem;
import com.atguigu.gmall.cart.CartService;
import com.atguigu.gmall.order.OrderService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.io.IOException;
import java.util.ArrayList;
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
    @Reference
    private CartService cartService;


    /**
     * 创建一个令牌，存储在redis中，用于和页面的进行比较
     *
     * @return
     */
    @Override
    public String createTradeToken() {

        String token = UUID.randomUUID().toString().replaceAll("-", "");
        Jedis jedis = jedisPool.getResource();
        jedis.setex(token, 60 * 5, "fanxiaoxiang");
        jedis.close();
        return token;
    }

    /**
     * 验证令牌，由于令牌验证后也需要删除，可以直接用redis的删除方法，删除成功返回1，失败返回0
     *
     * @param tradeToken
     * @return
     */
    @Override
    public boolean verfyToken(String tradeToken) {
        Jedis jedis = jedisPool.getResource();
        Long del = jedis.del(tradeToken);
        jedis.close();
        return del == 1 ? true : false;
    }

    /**
     * 验证库存是否充足
     *
     * @param userId 当前登录用户的id
     * @return 所有库存不足的商品的商品id
     */
    @Override
    public List<String> verfyStock(int userId) throws IOException {
        //查出当前用户购物车勾选的所有商品
        List<CartItem> cartItems = cartService.getCartInfoCheckedList(userId);
        List<String> result = new ArrayList<>();
        //遍历这些商品，然后查询每个商品是否库存量充足，如果不充足，返回库存不足的商品id
        for (CartItem cartItem : cartItems) {
            boolean b = stockCheck(cartItem.getSkuItem().getId(), cartItem.getNum());
            if(!b) {
                result.add(cartItem.getSkuItem().getSkuName());
            }
        }
        return result;
    }


    /**
     * 根据商品id和所需数量，查询库存是否足够
     * @param skuId
     * @param num
     * @return
     */
    private boolean stockCheck(Integer skuId, Integer num) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Get请求
        HttpGet httpGet = new HttpGet("http://www.gware.com/hasStock?skuId=" + skuId + "&num=" + num);
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity entity = response.getEntity();
            String data = EntityUtils.toString(entity);
            return  "0".equals(data)?false:true;
        } finally {
            //关响应
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


