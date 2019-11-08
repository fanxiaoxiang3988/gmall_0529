package com.atguigu.gmall.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.util.Base64Utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/9/5 0005
 */
public class CookieUtils {

    //构造器私有化
    private CookieUtils(){}

    /**
     * 添加cookie
     *
     * @param response
     * @param name
     * @param value
     * @param maxAge
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if (maxAge > 0) {
            cookie.setMaxAge(maxAge);
        }
        response.addCookie(cookie);
    }

    /**
     * 删除cookie
     *
     * @param response
     * @param name
     */
    public static void removeCookie(HttpServletResponse response, String name) {
        Cookie uid = new Cookie(name, null);
        uid.setPath("/");
        uid.setMaxAge(0);//同名cookie没有生存时间，就删除
        response.addCookie(uid);
    }

    /**
     * 获取cookie值
     *
     * @param request
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie cookies[] = request.getCookies();
        if(cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 根据用户传过来的token，解析得到jwt的第二段，取出其封装的用户基本信息并返回
     * @param token
     * @return
     */
    public static Map<String, Object> resolveTokenData(String token) {
        String[] split = token.split("\\.");
        if(split != null && split.length == 3){
            String s = split[1];
            byte[] bytes = Base64Utils.decodeFromString(s);
            String json = new String(bytes);
            return JSON.parseObject(json, Map.class);
        }
        return null;
    }

}
