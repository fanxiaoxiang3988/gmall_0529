package com.atguigu.gmall.payment.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.atguigu.gmall.order.OrderInfo;
import com.atguigu.gmall.order.OrderService;
import com.atguigu.gmall.pay.AlipayRequestVo;
import com.atguigu.gmall.payment.config.GmallAlipayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/11/26 0026
 */
@Slf4j
@Controller
public class PaymentController {

    @Reference
    private OrderService orderService;


    /**
     * 处理支付请求
     * @param vo 支付宝需要的订单数据
     * @param id 我们在OrderController中创建的订单的id，根据这个id查出订单的价钱，进行验价
     * @return
     * produces:它的作用是指定返回值类型，不但可以设置返回值类型还可以设定返回值的字符编码；（告诉浏览器，返回的是一个网页）
     * 和response.setContentType()作用相同
     */
    @ResponseBody
    @RequestMapping(value = "/pay", produces = "text/html;charset=utf-8")
    public String paymentPage(AlipayRequestVo vo, Integer id) throws AlipayApiException {
        log.info("需要支付的订单信息是：{}", vo);
        //1、先判断传来的订单价格是否正确，如果不正确，则返回错误
        OrderInfo orderInfo = orderService.getOrderById(id);
        BigDecimal subtract = orderInfo.getTotalAmount().subtract(new BigDecimal(vo.getTotalAmount()));
        if(subtract.intValue() == 0) {
            //1、价格相同，可继续进行，开始调用阿里的服务来进行支付业务
                //1、获得初始化的AlipayClient
            AlipayClient alipayClient =
                    new DefaultAlipayClient(GmallAlipayConfig.gatewayUrl,
                            GmallAlipayConfig.app_id,
                            GmallAlipayConfig.merchant_private_key,
                            "json",
                            GmallAlipayConfig.charset,
                            GmallAlipayConfig.alipay_public_key,
                            GmallAlipayConfig.sign_type);

                //2、设置请求参数
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
                    //支付成功后需要跳转的页面
            alipayRequest.setReturnUrl(GmallAlipayConfig.return_url);
                    //支付成功后异步的通知调用
            alipayRequest.setNotifyUrl(GmallAlipayConfig.notify_url);
                //3、构造请求参数
            alipayRequest.setBizContent("{\"out_trade_no\":\""+ vo.getOutTradeNo() +"\","
                    //商户订单号，商户网站订单系统中唯一订单号，必填
                    + "\"total_amount\":\""+ vo.getTotalAmount() +"\","
                    //付款金额，必填
                    + "\"subject\":\""+ vo.getSubject() +"\","
                    //订单名称，必填
                    + "\"body\":\""+ vo.getBody() +"\","
                    //商品描述，可空
                    + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
                //4、发送支付请求，我们将订单信息准备好，给支付宝发送请求
            String result = alipayClient.pageExecute(alipayRequest).getBody();
                System.out.println(result);
            return result;
        } else {
            return "订单非法";
        }
    }

    /**
     * 支付宝的同步方法，跳转到订单列表页
     * @return
     */
    @RequestMapping("/orderList")
    public String orderList() {
        return "redirect:http://order.gmall.com/list";
    }

    /**
     * 支付宝的异步方法
     * 在这里可以处理一些异步方法，更新订单状态信息
     * 支付宝每个订单异步回调通知的频率是15s 3m 10m 30m 30m 1h 2h 6h 15h，我们可以响应success就好了
     * 响应了success之后，就不会频繁通知了
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateOrderStatus")
    public String updateOrderStatus() {
        log.info("支付宝执行了异步回调函数。。。。");
        return "success";
    }


}
