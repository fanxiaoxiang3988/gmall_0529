package com.atguigu.gmall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/11/18 0018
 */
@Controller
public class TestAjax {

    @RequestMapping("/returnHtml")
    public String returnHtml() {
        return "testAjax";
    }


    @ResponseBody
    @RequestMapping("/testAjax")
    public String testAjax() {
        return "ok";
    }

}
