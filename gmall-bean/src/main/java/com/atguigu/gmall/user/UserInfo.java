package com.atguigu.gmall.user;

import com.atguigu.gmall.SuperBean;
import lombok.Data;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/8/18 0018
 */
@Data
public class UserInfo extends SuperBean {

    private String loginName;
    private String nickName;
    private String passwd;
    private String name;
    private String phoneNum;
    private String email;
    private String headImg;
    private String userLevel;

}
