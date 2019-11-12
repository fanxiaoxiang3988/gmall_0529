package com.atguigu.gmall.cart;

import java.util.List;

/**
 * 购物车功能
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/10/9 0009
 */
public interface CartService {

    /**
     * 未登录情况下，添加商品到购物车
     * @param skuId 商品id
     * @param cartKey 未登录情况下，这个购物车的id
     * @param num 数量
     * @return
     */
    String addToCartUnLogin(Integer skuId, String cartKey, Integer num);

    /**
     * 登陆情况下，添加商品到购物车
     * @param skuId
     * @param userId 用户id
     * @param num
     */
    void addToCartLogin(Integer skuId, Integer userId, Integer num);

    /**
     * 获取购物车数据
     * @param cartKey 购物车在redis中保存用的key
     * @return
     */
    CartVo getYourCart(String cartKey);

    /**
     * 合并购物车
     * @param cartKey
     * @param userId
     */
    void mergeCart(String cartKey, Integer userId);

    /**
     * 查询购物车所有数据
     * @param cartKey
     * @param login
     * @return
     */
    List<CartItem> getCartInfoList(String cartKey, boolean login);

    /**
     * 查询购物车中，某个商品的数据
     * @param cartKey
     * @param skuId
     * @return
     */
    CartItem getCartItemInfo(String cartKey, Integer skuId);

    void checkItem(Integer skuId, Boolean checkFlag, String tempCartKey, int userId, boolean loginFlag);
}
