package com.atguigu.gmall.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.annotation.LoginRequired;
import com.atguigu.gmall.manager.SkuEsService;
import com.atguigu.gmall.manager.es.SkuSearchResultEsVo;
import org.springframework.stereotype.Controller;
import com.atguigu.gmall.manager.es.SkuSearchParamEsVo;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/8/6 0006
 */
@Controller
public class ListController {

    @Reference
    private SkuEsService skuEsService;

    @RequestMapping("/list.html")
    public String listPage(SkuSearchParamEsVo paramEsVo, Model model) {
        //从ES中查询出商品搜索页面展示的内容
        SkuSearchResultEsVo searchResult = skuEsService.searchSkuFromES(paramEsVo);
        model.addAttribute("searchResult", searchResult);
        return "list";
    }

    @LoginRequired
    @RequestMapping("/hehe")
    public String hehehe() {
        return "hehe";
    }

}
