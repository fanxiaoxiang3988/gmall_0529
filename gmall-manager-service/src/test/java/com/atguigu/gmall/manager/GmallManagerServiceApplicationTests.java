package com.atguigu.gmall.manager;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.manager.mapper.BaseCatalog1Mapper;
import com.atguigu.gmall.manager.mapper.UserMapper;
import com.atguigu.gmall.manager.sku.SkuAttrValue;
import com.atguigu.gmall.manager.sku.SkuImage;
import com.atguigu.gmall.manager.sku.SkuInfo;
import com.atguigu.gmall.manager.sku.SkuSaleAttrValue;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    public void testSkuInfoJson(){

        /**
         *     private Integer skuId;//当前图片对应的skuId
         private String imgName;//图片的名字
         private String imgUrl;//图片的url
         private Integer spuImgId;//图片对应的spu_image表中的id
         private String isDefault;//是否默认图片
         */
        List<SkuImage> skuImages = new ArrayList<>();
        skuImages.add(new SkuImage(1,"黑色正面","hei.jpg",111,"0"));
        skuImages.add(new SkuImage(1,"黑色范面","heifan.jpg",112,"1"));


        /**
         *     private Integer attrId;//平台属性id
         private Integer valueId;//平台属性值id
         private Integer skuId;//对应的skuId
         */
        List<SkuAttrValue> skuAttrValues = new ArrayList<>();
        skuAttrValues.add(new SkuAttrValue(1,1,1));
        skuAttrValues.add(new SkuAttrValue(2,2,1));

        /**
         *     //sku_id  sale_attr_id  sale_attr_value_id  sale_attr_name  sale_attr_value_name
         private Integer skuId;//21
         private Integer saleAttrId;//销售属性的id
         private String saleAttrName;//销售属性的名字（冗余） ===【颜色】

         private Integer saleAttrValueId;//销售属性值id
         private Integer saleAttrValueName;//销售属性值的名字  ====【红色】
         */
        List<SkuSaleAttrValue> skuSaleAttrValues = new ArrayList<>();
        skuSaleAttrValues.add(new SkuSaleAttrValue(1,1,"颜色",1,"黑色"));
        skuSaleAttrValues.add(new SkuSaleAttrValue(1,2,"版本",1,"6+64GB"));

		SkuInfo skuInfo = new SkuInfo(51, new BigDecimal("50.99"), "三星 Glaxxxx9", "稳定爆炸..",
				new BigDecimal("19.99"), 1, 3,
				"http://xxxx.jpg",
				skuImages, skuAttrValues, skuSaleAttrValues);

        String s = JSON.toJSONString(skuInfo);
        System.out.println(s);


    }

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
