package com.atguigu.gmall.manager;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo//开启dubbo服务
@MapperScan("com.atguigu.gmall.manager.mapper")//扫描所有mybatis的mapper文件
@SpringBootApplication
public class GmallManagerServiceApplication {


    /**
     * 即使不依赖web容器，启动起来时dubbo会自动阻塞主线程，防止主线程和dubbo停止
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(GmallManagerServiceApplication.class, args);
    }

}
