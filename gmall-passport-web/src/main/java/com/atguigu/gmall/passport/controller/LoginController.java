package com.atguigu.gmall.passport.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.constant.CookieConstant;
import com.atguigu.gmall.passport.utils.JwtUtils;
import com.atguigu.gmall.user.UserInfo;
import com.atguigu.gmall.user.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

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
     * 前往登陆页面（登陆的方法）
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
                Map<String,Object> body = new HashMap<>();
                body.put("id",login.getId());
                body.put("loginName",login.getLoginName());
                body.put("nickName",login.getNickName());
                body.put("headImg",login.getHeadImg());
                body.put("email",login.getEmail());
                String newToken = JwtUtils.createJwtToken(body);
                if(login != null) {
                    //登陆成功，回到最开始请求的路径，并设置cookie
                    Cookie cookie = new Cookie(CookieConstant.SSO_COOKIE_NAME, newToken);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    if(!StringUtils.isEmpty(originUrl)) {
                        return "redirect:"+originUrl+"?token="+newToken;
                    } else {
                        return "redirect:http://www.gmall.com";
                    }
                } else {
                    return "index";
                }
            }
        }
    }

    /**
     * 为了安全性考虑，JwtUtils工具类和验证令牌是否合法的操作，都应只写在gmall-passport-web这一个模块中
     * 其他模块想要验证令牌是否合法时，只需要向gmall-passport-web发送一个请求验证即可，不需要在其他模块中写入验证逻辑
     * @param token
     * @return
     */
    @ResponseBody
    @RequestMapping("/confirmToken")
    public String confirmToken(String token) {
        boolean b = JwtUtils.confirmJwtToken(token);
        return b?"ok":"error";
    }


}
