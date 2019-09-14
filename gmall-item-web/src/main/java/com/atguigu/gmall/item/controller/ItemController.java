package com.atguigu.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.annotation.LoginRequired;
import com.atguigu.gmall.constant.CookieConstant;
import com.atguigu.gmall.manager.SkuEsService;
import com.atguigu.gmall.manager.SkuService;
import com.atguigu.gmall.manager.sku.SkuAttrValueMappingTo;
import com.atguigu.gmall.manager.sku.SkuInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/29 0029
 */
@Slf4j
@Controller
public class ItemController {

    @Reference
    private SkuService skuService;
    @Reference
    private SkuEsService skuEsService;


    @RequestMapping("/{skuId}.html")
    public String itemPage(@PathVariable("skuId") Integer skuId, Model model) {
        //1、查出sku详细信息
        SkuInfo skuInfo = null;
        try {
            skuInfo = skuService.getSkuInfoBySkuId(skuId);
        } catch (InterruptedException e) {
            log.debug("查询sku信息错误");
        }
        model.addAttribute("skuInfo", skuInfo);
        //2、查出当前sku对应的spu下的，所有的sku销售属性值的组合,效果如下
        /**
         * sku_id  spu_id      sku_name         sale_attr_value_id       sale_attr_value_name
         *   30      55       小米8青春版          117,118               深空土灰,土豪版
         *   31      55       小米8美人版          120,117               美人版,深空土灰
         */
        Integer spuId = skuInfo.getSpuId();
        List<SkuAttrValueMappingTo> valueMappingTos = skuService.getSkuAttrValueMapping(spuId);
        model.addAttribute("skuValueMapping", valueMappingTos);
        //3、增加点击率，更改es数据库中hotScore的评分（这一部分可以设为异步的方法）
        skuService.incrSkuHotScore(skuId);
        return "item";
    }

    /**
     * 测试thymeleaf语法
     */
    @RequestMapping("/thymeleafTest")
    public String thymeleafTest(Model model) {
        //model.addAttribute()就相当于request.setAttribute()
        model.addAttribute("divId","17");
        model.addAttribute("msg","Hello Violet");

        Map<String, String> map1 = new HashMap<>();
        map1.put("name", "辰东");
        map1.put("book", "完美世界");
        Map<String, String> map2 = new HashMap<>();
        map2.put("name", "肘子");
        map2.put("book", "大王饶命");
        Map<String, String> map3 = new HashMap<>();
        map3.put("name", "土豆");
        map3.put("book", "斗破苍穹");
        List<Map<String, String>> maps = Arrays.asList(map1, map2, map3);
        model.addAttribute("books", maps);
        return "thymeleafTest";
    }

    //使用注解的方式，统一管理相同逻辑的代码
    @LoginRequired
    @RequestMapping("/haha")
    public String hahaha(HttpServletRequest request) {
        Map<String, Object> userInfo = (Map<String, Object>)request.getAttribute("userInfo");
        log.info("我们封装的用户信息是：{}",userInfo);
        return "haha";
    }

}
