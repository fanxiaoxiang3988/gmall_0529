package com.atguigu.gmall.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.annotation.LoginRequired;
import com.atguigu.gmall.order.OrderInfo;
import com.atguigu.gmall.order.OrderInfoTo;
import com.atguigu.gmall.order.OrderService;
import com.atguigu.gmall.order.OrderSubmitVo;
import com.atguigu.gmall.user.UserAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/11/18 0018
 */
@Slf4j
@Controller
public class OrderController {

    @Reference
    private OrderService orderService;

    /**
     * 提交订单，首先进行防重复提交
     * 令牌机制：
     *      1、当前页面直接刷新
     *      2、回退到上一页面，再重新来一次（再重新点击提交）
     * 共同点：都是之前提交过的数据，再提交一次
     * 思路：每次提交，都携带唯一令牌，令牌使用过后就删除
     *      1、页面提交的时候，判断这个令牌是否是第一次用，如果是，则执行方法，不是第一次用，则返回
     *      2、回退浏览器，显示的是之前的缓存，并不会更新令牌，只有刷新页面，才会更新令牌
     * @param submitVo
     * @param request
     * @return
     */
    @LoginRequired
    @RequestMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo submitVo, HttpServletRequest request, Model model) throws IOException {
        Map<String, Object> userInfo = (Map<String, Object>) request.getAttribute("userInfo");
        log.info("当前的用户信息是:{}", userInfo);
        log.info("页面收到的数据是：{}", submitVo);
        //验证令牌是否失效，成功继续执行，失效则前往错误页面（防重复提交）
        boolean token = orderService.verfyToken(submitVo.getTradeToken());
        if(!token) {
            //令牌失效
            request.setAttribute("errorMsg", "订单信息失效，请去购物车重新刷新并下单");
            return "tradeFail";
        }
        //验证库存，库存充足则继续，不足则前往失败页面
        int userId = Integer.parseInt(userInfo.get("id") + "");
        List<String> stockNotGou = orderService.verfyStock(userId);
        if(stockNotGou!=null && stockNotGou.size()>0) {
            //令牌失效
            String string = JSON.toJSONString(stockNotGou);
            request.setAttribute("errorMsg", "购物车中商品库存不足：" + string);
            return "tradeFail";
        }
        //封装一些订单中用户收货的信息，便于订单生成
        OrderInfoTo orderInfoTo = new OrderInfoTo();
        orderInfoTo.setOrderComment(submitVo.getOrderComment());
        Integer userAddressId = submitVo.getUserAddressId();
        UserAddress userAddress = orderService.getUserAddressById(userAddressId);
        orderInfoTo.setConsignee(userAddress.getConsignee());
        orderInfoTo.setConsigneeTel(userAddress.getPhoneNum());
        orderInfoTo.setDeliveryAddress(userAddress.getUserAddress());
        //以上都没错,下单
        OrderInfo info = null;
        try {
            info = orderService.createOrder(userId,orderInfoTo);
        } catch (Exception e) {
            request.setAttribute("errorMsg", "网络异常..." + e.getMessage());
            e.printStackTrace();
            return "tradeFail";
        }
        /**
         * model.addAttribute()，如果是转发，则是放在请求域中，如果是重定向，内容会拼接在url地址中
         * 将支付宝生成订单时所需要的信息，放在请求域中
         * <input name="out_trade_no" type="hidden" /><br/>
         * <input name="subject" type="hidden" /><br/>
         * <input name="total_amount" type="hidden" /><br/>
         * <input name="body" type="hidden" /><br/>
         */
        model.addAttribute("orderInfo", info);

        return "paymentPage";
    }


}
