package com.atguigu.gmall.cart.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.annotation.LoginRequired;
import com.atguigu.gmall.cart.CartItem;
import com.atguigu.gmall.cart.CartService;
import com.atguigu.gmall.cart.CartVo;
import com.atguigu.gmall.constant.CookieConstant;
import com.atguigu.gmall.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/9/17 0017
 */
@Controller
public class CartController {

    @Reference
    private CartService cartService;

    /**
     * 将商品添加进购物车
     * @param skuId 商品的skuId
     * @param num 添加到购物车的数量
     * @return
     */
    @LoginRequired(needLogin = false)
    @RequestMapping("/addToCart")
    public String addToCart(Integer skuId, Integer num, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> loginUser = (Map<String, Object>)request.getAttribute(CookieConstant.LOGIN_USER_INFO_KEY);
        //判断用户是否登录，登录了用user:cart:用户id:info在redis中存储，没有登录,制造一个临时的购物车id,response.addCookie("cart_key","内容")
        String cartKey = null;
        if(loginUser == null) {
            //没有登录，再判定用户是否之前在未登录的时候，是否已经添加过商品，创建了临时购物车
            cartKey = CookieUtils.getCookieValue(request, CookieConstant.COOKIE_CART_KEY);
            if(StringUtils.isEmpty(cartKey)) {
                //还没有创建临时购物车，添加商品并创建一个临时购物车，将临时购物车的id存入cookie中
                //返回的cartId是购物车在redis中存储所用的key,也是我们临时购物车cart_key在cookie中对应的值
                cartKey = cartService.addToCartUnLogin(skuId, null, num);
                Cookie cookie = new Cookie(CookieConstant.COOKIE_CART_KEY, cartKey);
                cookie.setMaxAge(CookieConstant.COOKIE_CART_KEY_MAX_AGE);
                response.addCookie(cookie);
            } else {
                //已经创建过了临时购物车
                cartService.addToCartUnLogin(skuId, cartKey, num);
            }

        } else {
            //登录了
            Integer userId = Integer.parseInt(loginUser.get("id").toString());
            //合并购物车
            cartKey = CookieUtils.getCookieValue(request, CookieConstant.COOKIE_CART_KEY);
            if(StringUtils.isEmpty(cartKey)) {
                //之前没有临时购物车，直接添加即可
                cartService.addToCartLogin(skuId, userId, num);
            } else {
                //有临时购物车，先合并，再添加
                cartService.mergeCart(cartKey, userId);
                cartService.addToCartLogin(skuId, userId, num);
                //删掉未登录时cart_key这个cookie
                CookieUtils.removeCookie(response, CookieConstant.COOKIE_CART_KEY);
            }
        }
        //查出这个用户的这个商品在redis中存储的信息，以便回显
        CartItem cartItem = cartService.getCartItemInfo(cartKey, skuId);
        request.setAttribute("skuInfo", cartItem);
        return "success";
    }


    /**
     * 查看购物车的数据
     * @param request
     * @param response
     * @return
     */
    @LoginRequired(needLogin = false)
    @RequestMapping("/cartList")
    public String cartInfoPage(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> userInfo = (Map<String, Object>)request.getAttribute(CookieConstant.LOGIN_USER_INFO_KEY);
        //判断是否登陆并且有临时购物车（如果cookie中有cart_key，则证明有临时购物车）
        String tempCart = CookieUtils.getCookieValue(request, CookieConstant.COOKIE_CART_KEY);
        if(!StringUtils.isEmpty(tempCart) && userInfo != null) {
            //说明有临时购物车且用户登录了，合并购物车
            cartService.mergeCart(tempCart, Integer.parseInt(userInfo.get("id").toString()));
            //删除当前临时购物车的cookie
            Cookie cookie = new Cookie(CookieConstant.COOKIE_CART_KEY, "fanxiaoxiang");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        boolean login = false;
        String cartKey = "";
        if(userInfo != null) {
            //登陆了，cartKey传的就是用户id，使用用户id可以拼出redis的key
            login = true;
            cartKey = userInfo.get("id").toString()+"";
        } else {
            //未登录，cartKey就是"cart_key"这个字符串在cookie中查出的对应的值
            login = false;
            cartKey = CookieUtils.getCookieValue(request, CookieConstant.COOKIE_CART_KEY);
        }
        List<CartItem> cartItems = cartService.getCartInfoList(cartKey, login);
        //购物车对象的封装
        CartVo cartVo = new CartVo();
        cartVo.setCartItems(cartItems);
        cartVo.setTotalPrice(cartVo.getTotalPrice());
        request.setAttribute("cartVo", cartVo);
        return "cartList";
    }


    /**
     * 购物车页面的勾选按钮（勾选了以后修改CartItem的isCheck字段以记录勾选状态）
     * @param skuId
     * @param checkFlag
     * @param request
     * @return
     */
    @LoginRequired(needLogin = false)
    @ResponseBody
    @RequestMapping("/checkItem")
    public String checkItem(Integer skuId,Boolean checkFlag,HttpServletRequest request) {
        //获取用户信息
        Map<String, Object> userInfo = (Map<String, Object>)request.getAttribute(CookieConstant.LOGIN_USER_INFO_KEY);
        int userId = 0;
        try {
            userId = Integer.parseInt(userInfo.get("id").toString());
        } catch (Exception e){}
        //获取临时购物车
        String tempCartKey = CookieUtils.getCookieValue(request, CookieConstant.COOKIE_CART_KEY);
        //判断用户是否登录
        boolean loginFlag = userInfo == null ? false:true;
        cartService.checkItem(skuId,checkFlag,tempCartKey,userId,loginFlag);
        return "ok";
    }

}
