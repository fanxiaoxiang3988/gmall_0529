package com.atguigu.gmall.user;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/8/23 0023
 */
public interface UserInfoService {

    /**
     * 按照传来的账号密码，查出用户的详情
     * @param userInfo
     * @return
     */
    public UserInfo login (UserInfo userInfo);

}
