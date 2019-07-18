package com.atguigu.gmall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/14 0014
 */
@Controller
public class MainController {

    @RequestMapping("/main")
    public String hello(){

        //ThymeleafProperties里是thymeleaf所有的默认配置
        /**
         * thymeleaf 由视图解析器拼串得到地址：前缀。后缀
         * public static final String DEFAULT_PREFIX = "classpath:/templates/";
         * public static final String DEFAULT_SUFFIX = ".html";
         */
        return "main";
    }

}
