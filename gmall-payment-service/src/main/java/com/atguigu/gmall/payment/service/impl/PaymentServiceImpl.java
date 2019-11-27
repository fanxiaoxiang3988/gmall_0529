package com.atguigu.gmall.payment.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.pay.PaymentInfo;
import com.atguigu.gmall.pay.PaymentService;
import com.atguigu.gmall.payment.mapper.PaymentInfoMapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/11/27 0027
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentInfoMapper paymentInfoMapper;

    /**
     * 保存支付宝流水的初始化信息
     * @param paymentInfo
     */
    @Async
    @Override
    public void createAliTrade(PaymentInfo paymentInfo) {
        paymentInfoMapper.insert(paymentInfo);
    }

    /**
     * 修改支付宝流水信息
     * @param paymentInfo
     */
    @Override
    public void updatePayement(PaymentInfo paymentInfo) {

        String outTradeNo = paymentInfo.getOutTradeNo();
        UpdateWrapper<PaymentInfo> paymentInfoUpdateWrapper = new UpdateWrapper<>();
        paymentInfoUpdateWrapper.eq("out_trade_no", outTradeNo);
        paymentInfoMapper.update(paymentInfo, paymentInfoUpdateWrapper);

    }


}
