package com.atguigu.gmall.search;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * gmall-search-service依赖了gmall-serviceutil，serviceutil导入了springboot的jdbc相关的依赖，所以search-service会自动引入jdbc相关的配置
 * 有三种解决方法：
 *      1、启动类标注exclude = DataSourceAutoConfiguration.class
 *      2、在pom文件中排除jdbc相关的依赖
 *      3.application中添加数据库配置（用不到mysql数据库，不考虑）
 */
@EnableAsync
@EnableDubbo
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class GmallSearchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallSearchServiceApplication.class, args);
    }

}
