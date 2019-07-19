package com.atguigu.gmall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/19 0019
 */
@Controller
@RequestMapping("/spu")
public class SpuManagerController {

    @RequestMapping("/listPage.html")
    public String spuListPage() {
        return "spu/spuListPage";
    }

}
