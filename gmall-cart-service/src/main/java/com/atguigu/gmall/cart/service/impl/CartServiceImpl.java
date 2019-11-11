package com.atguigu.gmall.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.cart.CartItem;
import com.atguigu.gmall.cart.CartService;
import com.atguigu.gmall.cart.CartVo;
import com.atguigu.gmall.cart.SkuItem;
import com.atguigu.gmall.cart.constent.CartConstant;
import com.atguigu.gmall.manager.SkuService;
import com.atguigu.gmall.manager.sku.SkuInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/10/9 0009
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private JedisPool jedisPool;
    @Reference
    private SkuService skuService;

    @Override
    public String addToCartUnLogin(Integer skuId, String cartKey, Integer num) {
        Jedis jedis = jedisPool.getResource();
        if(!StringUtils.isEmpty(cartKey)) {
            //之前创建过临时购物车
            Boolean exists = jedis.exists(cartKey);
            if(exists == false) {
                //证明redis中没有此cartKey对应的值，有可能是该商品过期，也可能是传过来的cartKey是非法的
                String newCartKey = createCart(skuId, num, false, null);
                return newCartKey;
            } else {
                String skuInfoJson = jedis.hget(cartKey, skuId + "");
                if(!StringUtils.isEmpty(skuInfoJson)) {
                    //购物车中有此商品，叠加数量即可
                    CartItem cartItem = JSON.parseObject(skuInfoJson, CartItem.class);
                    cartItem.setNum(cartItem.getNum()+num);
                    cartItem.setTotalPrice(cartItem.getTotalPrice());
                    String toJSONString = JSON.toJSONString(cartItem);
                    jedis.hset(cartKey, skuId+"", toJSONString);
                } else {
                    //购物车中无此商品，新增
                    try {
                        CartItem cartItem = new CartItem();
                        SkuItem skuItem = new SkuItem();
                        SkuInfo skuInfo = skuService.getSkuInfoBySkuId(skuId);
                        BeanUtils.copyProperties(skuInfo, skuItem);
                        cartItem.setSkuItem(skuItem);
                        cartItem.setNum(num);
                        cartItem.setTotalPrice(cartItem.getTotalPrice());
                        //新增商品
                        String jsonString = JSON.toJSONString(cartItem);
                        jedis.hset(cartKey, skuItem.getId()+"", jsonString);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            //之前没有临时购物车，需要新建临时购物车
            return createCart(skuId, num, false, null);
        }
        jedis.close();
        return cartKey;
    }

    @Override
    public void addToCartLogin(Integer skuId, Integer userId, Integer num) {
        Jedis jedis = jedisPool.getResource();
        Boolean exists = jedis.exists(CartConstant.USER_CART_PREFIX + userId);
        if(exists) {
            //当前用户已有购物车
            String cartKey = CartConstant.USER_CART_PREFIX + userId;
            String hget = jedis.hget(cartKey, skuId + "");
            if(!StringUtils.isEmpty(hget)) {
                //购物车中有这个商品，叠加数量即可
                CartItem cartItem = JSON.parseObject(hget, CartItem.class);
                cartItem.setNum(cartItem.getNum() + num);
                cartItem.setTotalPrice(cartItem.getTotalPrice());
                String s = JSON.toJSONString(cartItem);
                jedis.hset(cartKey, skuId+"", s);
            } else {
                try {
                    //购物车中没有这个商品
                    CartItem cartItem = new CartItem();
                    SkuItem skuItem = new SkuItem();
                    SkuInfo skuInfoBySkuId = skuService.getSkuInfoBySkuId(skuId);
                    BeanUtils.copyProperties(skuInfoBySkuId, skuItem);
                    cartItem.setSkuItem(skuItem);
                    cartItem.setNum(num);
                    cartItem.setTotalPrice(cartItem.getTotalPrice());
                    String json = JSON.toJSONString(cartItem);
                    jedis.hset(cartKey, skuId+"", json);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            //当前用户没有购物车，需要新建购物车，然后再添加商品
            createCart(skuId, num, true, userId);
        }
    }

    @Override
    public CartVo getYourCart(String cartKey) {
        return null;
    }

    /**
     * 合并购物车
     * @param cartKey
     * @param userId
     */
    @Override
    public void mergeCart(String cartKey, Integer userId) {
        //查出临时购物车中的所有数据
        List<CartItem> cartItems = getCartInfoList(cartKey, false);
        //把这些数据添加到用户购物车中
        if(cartItems != null && cartItems.size()>0) {
            for (CartItem tempCartItem : cartItems) {
                addToCartLogin(tempCartItem.getSkuItem().getId(), userId, tempCartItem.getNum());
            }
        }
        //删除临时购物车
        Jedis jedis = jedisPool.getResource();
        jedis.del(cartKey);
    }

    /**
     * 根据传过来的临时购物车或者用户id，查询对应的购物车的所有数据
     * @param cartKey 登陆了的话，传过来的是用户id
     * @param login
     * @return
     */
    @Override
    public List<CartItem> getCartInfoList(String cartKey, boolean login) {
        String queryKey = cartKey;
        if(login) {
            //登陆了
            queryKey = CartConstant.USER_CART_PREFIX + cartKey;
        }
        //查询reids中，这个key对应的购物车数据
        Jedis jedis = jedisPool.getResource();
        List<CartItem> cartItemList = new ArrayList<>();
        Map<String, String> hgetAll = jedis.hgetAll(queryKey);
        for (Map.Entry<String, String> entry : hgetAll.entrySet()) {
            //String key = entry.getKey();
            String value = entry.getValue();
            //转化
            CartItem cartItem = JSON.parseObject(value, CartItem.class);
            cartItemList.add(cartItem);
        }
        return cartItemList;
    }

    @Override
    public CartItem getCartItemInfo(String cartKey, Integer skuId) {
        Jedis jedis = jedisPool.getResource();
        String json = jedis.hget(cartKey, skuId + "");
        CartItem cartItem = JSON.parseObject(json, CartItem.class);
        return cartItem;
    }

    /**
     * 新建购物车并添加指定商品进购物车
     * @param skuId 商品skuId
     * @param num 数量
     * @param login 是否登录
     * @param userId 用户id
     * @return
     */
    private String createCart(Integer skuId, Integer num, boolean login, Integer userId) {
        Jedis jedis = jedisPool.getResource();
        String newCartKey;
        if(login) {
            //已登录用的newCartKey
            newCartKey = CartConstant.USER_CART_PREFIX + userId;
        } else {
            //未登录用的newCartKey
            newCartKey = CartConstant.TEMP_CART_PREFIX + UUID.randomUUID().toString().substring(0,10).replaceAll("-","");
        }

        try {
            CartItem cartItem = new CartItem();
            SkuItem skuItem = new SkuItem();
            //获取到添加的这个商品的详细信息
            SkuInfo skuInfo = skuService.getSkuInfoBySkuId(skuId);
            //拷贝商品的详细信息进我们封装的对象里，准备存入到redis中
            BeanUtils.copyProperties(skuInfo, skuItem);
            cartItem.setSkuItem(skuItem);
            cartItem.setNum(num);
            cartItem.setTotalPrice(cartItem.getTotalPrice());
            String jsonString = JSON.toJSONString(cartItem);
            jedis.hset(newCartKey, skuItem.getId()+"", jsonString);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        jedis.close();
        return newCartKey;
    }

}
