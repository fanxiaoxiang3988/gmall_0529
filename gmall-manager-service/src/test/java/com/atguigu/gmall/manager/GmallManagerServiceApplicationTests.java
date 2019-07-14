package com.atguigu.gmall.manager;

import com.atguigu.gmall.manager.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 1、导入mybatis-plus的starter
 * 2、编写javaBean，编写mapper接口（集成basemapper）
 * 3.@MappeScan("com.atguigu.gmall.manager.mapper")指定扫描的mapper路径
 *  高级：
 *      逻辑删除
 *  		1、在application.properties说明逻辑删除的规则
 *  	    2、在javaBean里面加上逻辑删除字段并且用@TableLogic
 *  	    3、自定义一个mybatisplus的配置类，注入逻辑删除插件即可
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallManagerServiceApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testLogistciDelete() {
        userMapper.deleteById(1L);
        System.out.println("删除完成。。。");
        //逻辑删除以后，只是改变了del_flag字段（假删除），但是查询时查询不到
        //以后的所有查询默认都是查未删除的
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
            System.out.println(user);
        }
    }


    @Test
    public void contextLoads() {
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
            System.out.println(user);
        }
        System.out.println("========");
        //要让xml生效一定加上mybatis-plus.mapper-locations=classpath:mapper/*.xml
        User user = new User();
        user.setName("Jack");
        user.setAge(20);
        User userByHaha = userMapper.getUserByHaha(user);
        System.out.println(userByHaha);
    }

}
