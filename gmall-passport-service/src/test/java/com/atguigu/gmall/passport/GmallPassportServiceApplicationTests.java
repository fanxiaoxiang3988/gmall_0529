package com.atguigu.gmall.passport;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.MessageDigest;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class GmallPassportServiceApplicationTests {

    @Test
    public void contextLoads() {

        //DigestUtils默认加工出32位的结果
        String s = DigestUtils.md5Hex("123456");
        System.out.println(s);

    }

}
