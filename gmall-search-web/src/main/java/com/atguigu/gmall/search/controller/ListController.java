package com.atguigu.gmall.search.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/8/6 0006
 */
@Controller
public class ListController {

    @RequestMapping("/list.html")
    public String listPage() {
        return "list";
    }

}
