package com.atguigu.gmall.passport.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Map;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/9/9 0009
 */
public class JwtUtils {

    //我们自己定义一个密钥，不论制造令牌还是解析令牌，都需要用到这个密钥来鉴定真伪
    private final static String keyStr = "fanxiaoxiangandliwenxiaaabbccddeeffgghhiijjkkllmmnnooppqq";

    /**
     * 根据传来的想要保存的一些不重要的基本信息的用户信息，制造一个jwt令牌
     * @param body
     * @return
     */
    public static String createJwtToken(Map<String, Object> body) {
        SecretKey secretKey = Keys.hmacShaKeyFor(keyStr.getBytes());
        String compact = Jwts.builder()
                            .addClaims(body)
                            .signWith(secretKey, SignatureAlgorithm.HS256)
                            .compact();
        return compact;
    }

    /**
     * 解析令牌，看令牌是否合法
     * @param jwtToken
     * @return
     */
    public static boolean confirmJwtToken(String jwtToken) {
        SecretKey secretKey = Keys.hmacShaKeyFor(keyStr.getBytes());
        try {
            Jwt parse = Jwts.parser().setSigningKey(secretKey).parse(jwtToken);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
