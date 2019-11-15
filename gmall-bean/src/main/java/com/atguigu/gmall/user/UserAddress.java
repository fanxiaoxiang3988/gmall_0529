package com.atguigu.gmall.user;

import com.atguigu.gmall.SuperBean;
import lombok.Data;

/**
 * 用户收货地址
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/11/14 0014
 */
@Data
public class UserAddress extends SuperBean {

    private String  userAddress;
    private Integer userId;
    private String consignee;//收货人
    private String phoneNum;
    private String isDefault;//是否默认  1默认  0非默认

}
