package com.atguigu.gmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.manager.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * 这个controller来给easyui提供url访问，返回json数据
 * 为平台属性管理中三个下拉框返回所需要的数据
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/16 0016
 */
@RequestMapping("/basecatalog")
//@ResponseBody
//@Controller
@RestController
public class BaseCatalogRestController {

    @Reference
    private CatalogService catalogService;
    @Reference
    private BaseAttrInfoService baseAttrInfoService;

    /**
     * 查询所有的一级分类
     * 当前的web依赖了interface，所以可以直接调用interface的接口，又因为dubbo通信，可以调用service里面的实现
     * @return
     */
    @RequestMapping("/1/list.json")
    public List<BaseCatalog1> listBaseCatalog1() {
        return catalogService.getAllBaseCatalog1();
    }

    /**
     * 查询一级分类下的所有二级分类
     * @param id 一级分类id
     * @return
     */
    @RequestMapping("/2/list.json")
    public List<BaseCatalog2> listBaseCatalog2(Integer id) {
        return catalogService.getBaseCatalog2ByC1id(id);
    }

    /**
     * 查询二级分类下的所有三级分类
     * @param id 二级分类id
     * @return
     */
    @RequestMapping("/3/list.json")
    public List<BaseCatalog3> listBaseCatalog3(Integer id) {
        return catalogService.getBaseCatalog3ByC2id(id);
    }

    /**
     * 查询三级分类下的所有属性名
     * @param id 三级分类id
     * @return
     */
    @RequestMapping("/attrs.json")
    public List<BaseAttrInfo> getBaseAttrInfos(Integer id) {
       return baseAttrInfoService.getBaseAttrInfoByCatalog3Id(id);
    }

}
