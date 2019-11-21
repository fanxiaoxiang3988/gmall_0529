package com.atguigu.gmall.order;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class GmallOrderServiceApplicationTests {

    @Test
    public void contextLoads() {
        Date date = new Date(System.currentTimeMillis() + 1000 * 60 * 30);
        System.out.println(date);
    }

}
