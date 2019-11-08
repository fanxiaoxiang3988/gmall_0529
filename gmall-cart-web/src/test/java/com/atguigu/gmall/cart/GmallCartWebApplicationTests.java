package com.atguigu.gmall.cart;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;

import java.util.HashMap;
import java.util.Map;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallCartWebApplicationTests {

    @Test
    public void contextLoads() {

        Map<String, Object> map = new HashMap<>();
        map.put("id","1");
        map.put("name","fanxiaoxiang");
        String a = "fanxiaoxiang";
        String s = Base64Utils.encodeToString(a.getBytes());
        System.out.println("解析到的数据是："+s);
        byte[] bytes = Base64Utils.decodeFromString(s);
        String json = new String(bytes);
        System.out.println("反解得到的数据时："+json);

    }

}
