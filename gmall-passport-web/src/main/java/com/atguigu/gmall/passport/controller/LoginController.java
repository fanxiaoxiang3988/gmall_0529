package com.atguigu.gmall.passport.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.constant.CookieConstant;
import com.atguigu.gmall.user.UserInfo;
import com.atguigu.gmall.user.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/8/18 0018
 */
@Slf4j
@Controller
public class LoginController {

    @Reference
    private UserInfoService userInfoService;

    /**
     * 前往登陆页面
     * @return
     */
    @RequestMapping("/login")
    public String login(UserInfo userInfo, String originUrl,
                        @CookieValue(name = CookieConstant.SSO_COOKIE_NAME,required = false)String token,
                        HttpServletResponse response) {
        //登陆过了
        if(!StringUtils.isEmpty(token)) {
            //登陆过了就重定向到之前用户请求的页面
            return "redirect:"+originUrl+"?token="+token;
        } else {
            //没登陆过(判定用户是否填写了用户信息)
            if(StringUtils.isEmpty(userInfo.getLoginName())) {
                return "index";
            } else {
                //验证用户的账号密码是否正确
                UserInfo login = userInfoService.login(userInfo);
                String newToken = UUID.randomUUID().toString().replaceAll("-", "");
                if(login != null) {
                    //登陆成功，回到最开始请求的路径，并设置cookie
                    Cookie cookie = new Cookie(CookieConstant.SSO_COOKIE_NAME, newToken);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    return "redirect:"+originUrl+"?token="+newToken;
                } else {
                    return "index";
                }
            }
        }
    }


}
