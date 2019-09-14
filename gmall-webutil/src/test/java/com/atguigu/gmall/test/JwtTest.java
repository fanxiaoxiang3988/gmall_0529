package com.atguigu.gmall.test;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.Test;
import org.springframework.util.Base64Utils;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/9/9 0009
 */
public class JwtTest {

    private String key = "fanxiaoxiangandliwenxiaaabbccddeeffgg";

    @Test
    public void cresteJwt() {
        Map<String,Object> userInfo = new HashMap<>();
        userInfo.put("id","1");
        userInfo.put("userName","fanxiaoxaing");

        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());
        String string = Jwts.builder().addClaims(userInfo).signWith(secretKey).compact();
        System.out.println(string);
        //eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEiLCJ1c2VyTmFtZSI6ImZhbnhpYW94YWluZyJ9.MmNUewYW10YkFRQh-5bTjkVXUx9GPLZY8XfZCuMXxrU
    }

    @Test
    public void verf() {
        String s = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEiLCJ1c2VyTmFtZSI6ImZhbnhpYW94YWluZyJ9.MmNUewYW10YkFRQh-5bTjkVXUx9GPLZY8XfZCuMXxrU";
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());
        Object body = Jwts.parser().setSigningKey(secretKey).parse(s).getBody();
        System.out.println("body======" + body);
        //body======{id=1, userName=fanxiaoxaing}
    }

    @Test
    public void testResolveJwt() {

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEiLCJ1c2VyTmFtZSI6ImZhbnhpYW94YWluZyJ9.MmNUewYW10YkFRQh-5bTjkVXUx9GPLZY8XfZCuMXxrU";
        String[] split = token.split("\\.");
        String s = split[1];
        byte[] bytes = Base64Utils.decodeFromString(s);
        try {
            String json = new String(bytes, "utf-8");
            System.out.println(JSON.parseObject(json, Map.class));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

}
