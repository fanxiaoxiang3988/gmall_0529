package com.atguigu.gmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.manager.BaseAttrInfoService;
import com.atguigu.gmall.manager.BaseAttrValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/16 0016
 */
@RequestMapping("/attr")
@Controller
public class AttrManagerController {

    @Reference
    private BaseAttrInfoService baseAttrInfoService;

    /**
     * 去平台属性列表页面
     * 所有去页面的请求，都加上.html后缀
     * @return
     */
    @RequestMapping("/listPage.html")
    public String toAttrListPage() {
        return "attr/attrListPage";
    }

    /**
     * 获取某个平台属性下的所有属性值
     * @param id 属性名的id
     * @return
     */
    @ResponseBody
    @RequestMapping("/value/{id}")
    public List<BaseAttrValue> getBaseAttrValueByAttrId(@PathVariable("id") Integer id) {
        return baseAttrInfoService.getBaseAttrValueByAttrId(id);
    }

}
