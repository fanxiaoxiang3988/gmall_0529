package com.atguigu.gmall.order.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.cart.CartItem;
import com.atguigu.gmall.cart.CartService;
import com.atguigu.gmall.cart.CartVo;
import com.atguigu.gmall.cart.SkuItem;
import com.atguigu.gmall.constent.CartConstant;
import com.atguigu.gmall.order.*;
import com.atguigu.gmall.order.mapper.OrderDetailMapper;
import com.atguigu.gmall.order.mapper.OrderInfoMapper;
import com.atguigu.gmall.order.mapper.UserAddressMapper;
import com.atguigu.gmall.user.UserAddress;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;


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
     * 根据用户地址的id查询数据库对应的一条数据
     * @param userAddressId
     * @return
     */
    @Override
    public UserAddress getUserAddressById(Integer userAddressId) {
        return userAddressMapper.selectById(userAddressId);
    }

    /**
     * 创建订单
     * @param userId 用户的id
     * @param orderInfoTo 订单的备注、收货人等信息
     */
    @Override
    public void createOrder(int userId, OrderInfoTo orderInfoTo) {
        //1、查出该用户id对应redis中存储的所有结算商品
        List<CartItem> cartItems = cartService.getCartInfoCheckedList(userId);
        CartVo cartVo = new CartVo();
        cartVo.setCartItems(cartItems);
        BigDecimal totalPrice = cartVo.getTotalPrice();//计算总额
        //2、向OrderInfo表插入数据
        OrderInfo orderInfo = new OrderInfo();
            //先将默认的设置好
        orderInfo.setOrderStatus(OrderStatus.UNPAID);
        orderInfo.setProcessStatus(ProcessStatus.UNPAID);
        orderInfo.setPaymentWay(PaymentWay.ONLINE);
        orderInfo.setCreateTime(new Date());
        orderInfo.setExpireTime(new Date(System.currentTimeMillis()+1000*60*30));//30分钟
            //设置收货信息
        orderInfo.setUserId(userId);
        orderInfo.setOrderComment(orderInfoTo.getOrderComment());
        orderInfo.setConsignee(orderInfoTo.getConsignee());
        orderInfo.setConsigneeTel(orderInfoTo.getConsigneeTel());
        orderInfo.setDeliveryAddress(orderInfoTo.getDeliveryAddress());
            //设置对外业务号
        orderInfo.setOutTradeNo("ATGUIGU_" + System.currentTimeMillis() + "_" + userId);
        orderInfo.setTotalAmount(totalPrice);
        orderInfoMapper.insert(orderInfo);
        //3、向OrderDetail表循环插入数据（多个OrderDetail表可以汇总成一张OrderInfo表）
        for (CartItem cartItem : cartItems) {
            SkuItem skuItem = cartItem.getSkuItem();
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setImgUrl(skuItem.getSkuDefaultImg());
            orderDetail.setOrderId(orderInfo.getId());
            orderDetail.setOrderPrice(skuItem.getPrice());
            orderDetail.setSkuId(skuItem.getId());
            orderDetail.setSkuName(skuItem.getSkuName());
            orderDetail.setSkuNum(cartItem.getNum());
            orderDetailMapper.insert(orderDetail);
        }
        //4、删除购物车中这些商品的数据
        Jedis jedis = jedisPool.getResource();
        String[] delStrIds = new String[cartItems.size()];
        for (int i = 0; i < cartItems.size(); i++) {
            delStrIds[i] = cartItems.get(i).getSkuItem().getId()+"";
    }
        jedis.hdel(CartConstant.USER_CART_PREFIX+userId ,delStrIds);
        //5、修改购物车中关于存储顺序的fieldOrder字段
            //先查出之前的fieldOrder字段
        String fieldOrder = jedis.hget(CartConstant.USER_CART_PREFIX + userId, "fieldOrder");
        List list = JSON.parseObject(fieldOrder, List.class);
            //根据这些字段，重新去遍历，筛选出没有被选中的
        List<Integer> newfieldOrder = new ArrayList<>();
        for (Object o : list) {
            Integer id = Integer.parseInt(o.toString());
            boolean exist = false;
            for (CartItem cartItem : cartItems) {
                if(cartItem.getSkuItem().getId() == id) {
                    //原列表中的id项在删除项中有
                    exist = true;
                }
            }
            if(!exist) {
                newfieldOrder.add(id);
            }
        }
            //重新设置fieldOrder字段
        jedis.hset(CartConstant.USER_CART_PREFIX+userId,"fieldOrder",JSON.toJSONString(newfieldOrder));
        jedis.close();
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


