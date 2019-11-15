package com.atguigu.gmall.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.annotation.LoginRequired;
import com.atguigu.gmall.cart.CartItem;
import com.atguigu.gmall.cart.CartService;
import com.atguigu.gmall.cart.CartVo;
import com.atguigu.gmall.order.TradePageVo;
import com.atguigu.gmall.user.UserAddress;
import com.atguigu.gmall.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 交易结算
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/11/13 0013
 */
@Controller
public class TradeController {

    @Reference
    private CartService cartService;
    @Reference
    private UserService userService;

    @LoginRequired
    @RequestMapping("/trade")
    public String trade(HttpServletRequest request) {
        //获取到用户信息
        Map<String, Object> userInfo = (Map<String, Object>) request.getAttribute("userInfo");
        int id = Integer.parseInt(userInfo.get("id").toString());
        //获取购物车中被选中的商品
        List<CartItem> cartItemList = cartService.getCartInfoCheckedList(id);
        //查询和展示收货人的信息
        List<UserAddress> userAddresses = userService.getUserAddressesByUserId(id);
        //展示选中的购物车的商品的信息
        TradePageVo vo = new TradePageVo();
        CartVo cartVo = new CartVo();
        cartVo.setCartItems(cartItemList);
        BigDecimal totalPrice = cartVo.getTotalPrice();
        vo.setUserAddresses(userAddresses);
        vo.setCartItems(cartItemList);
        vo.setTotalPrice(totalPrice);
        request.setAttribute("tradeInfo", vo);
        //防止重复提交
        return "trade";
    }

}
