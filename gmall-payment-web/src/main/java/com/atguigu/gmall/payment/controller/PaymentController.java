package com.atguigu.gmall.payment.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.order.OrderService;
import com.atguigu.gmall.pay.AlipayRequestVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * @param vo
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/pay")
    public String paymentPage(AlipayRequestVo vo, Integer id) {
        log.info("需要支付的订单信息是：{}", vo);

        return null;
    }


}
