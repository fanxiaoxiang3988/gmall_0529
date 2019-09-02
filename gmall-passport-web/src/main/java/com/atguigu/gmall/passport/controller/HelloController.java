package com.atguigu.gmall.passport.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/8/18 0018
 */
@RestController
public class HelloController {

    @RequestMapping("/ok")
    public String hello(){
        return "ok";
    }
}
