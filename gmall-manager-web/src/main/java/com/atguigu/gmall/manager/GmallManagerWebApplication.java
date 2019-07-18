package com.atguigu.gmall.manager;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * thymeleaf的使用
 * 1、在web-utils引入thymeleaf的starter，本项目依赖于web-util
 * 2、让thymeleaf不要进行语法的严格校验
 *
 */
@EnableDubbo
@SpringBootApplication
public class GmallManagerWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallManagerWebApplication.class, args);
    }

}
