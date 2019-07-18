package com.atguigu.gmall.manager;

import com.atguigu.gmall.manager.mapper.BaseCatalog1Mapper;
import com.atguigu.gmall.manager.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallManagerServiceApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BaseCatalog1Mapper baseCatalog1Mapper;
    @Autowired
    private CatalogService catalogService;

    @Test
    public void testCatalogService() {
        List<BaseCatalog1> allBaseCatalog1 = catalogService.getAllBaseCatalog1();
        log.info("一级分类信息：{}",allBaseCatalog1);
        List<BaseCatalog2> baseCatalog2ByC1id = catalogService.getBaseCatalog2ByC1id(allBaseCatalog1.get(0).getId());
        log.info("{}的二级分类信息是：{}",allBaseCatalog1.get(0),baseCatalog2ByC1id);
        List<BaseCatalog3> baseCatalog3ByC2id = catalogService.getBaseCatalog3ByC2id(baseCatalog2ByC1id.get(0).getId());
        log.info("{}的三级分类信息是：{}",baseCatalog2ByC1id.get(0),baseCatalog3ByC2id);

    }

    @Test
    public void testMapper() {
        BaseCatalog1 baseCatalog1 = new BaseCatalog1();
        baseCatalog1.setName("测试");
        baseCatalog1Mapper.insert(baseCatalog1);
        log.info("添加数据成功，id是：{},name是：{}",baseCatalog1.getId(),baseCatalog1.getName());
        //System.out.println("添加信息成功");
    }

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
