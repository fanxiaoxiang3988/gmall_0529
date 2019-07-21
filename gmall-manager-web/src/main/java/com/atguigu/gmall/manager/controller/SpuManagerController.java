package com.atguigu.gmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.manager.SpuInfoService;
import com.atguigu.gmall.manager.spu.SpuInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/19 0019
 */
@Controller
@RequestMapping("/spu")
public class SpuManagerController {

    @Reference
    private SpuInfoService spuInfoService;

    @RequestMapping("/listPage.html")
    public String spuListPage() {
        return "spu/spuListPage";
    }


    //@RequestMapping("/info.json/{c3id}")//@RequestParam("catalog3Id") Integer catalog3Id
    //public List<SpuInfo> getSpuInfoByC3Id(@PathVariable("c3id") Integer c3id) {
    //public List<SpuInfo> getSpuInfoByC3Id(@PathVariable Integer c3id) {
    @ResponseBody
    @RequestMapping("/info.json")
    public List<SpuInfo> getSpuInfoByC3Id(@RequestParam Integer catalog3Id) {


        return spuInfoService.getSpuInfoByC3Id(catalog3Id);

    }


}
