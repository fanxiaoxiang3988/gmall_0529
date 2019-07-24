package com.atguigu.gmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.manager.SpuInfoService;
import com.atguigu.gmall.manager.spu.BaseSaleAttr;
import com.atguigu.gmall.manager.spu.SpuInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/19 0019
 */
@Slf4j
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

    /**
     * 返回所有的销售属性
     * @return
     */
    @ResponseBody
    @RequestMapping("/base_sale_attr")
    public List<BaseSaleAttr> getBaseSaleAttr(){
        return  spuInfoService.getBaseSaleAttr();
    }

    /**
     * 页面提交来的数据：
     *  SpuInfo(spuName=华为,
     *  description=华为mate10,
     *  catalog3Id=61,
     *  tmId=null,
     *  spuImages=[SpuImage(spuId=null, imgName=1562297375(1).jpg, imgUrl=http://file.gmall.com/group1/M00/00/00/wKjYgF030s-AEnauAACc4QxTmOM252.jpg)],
     *  spuSaleAttrs=[SpuSaleAttr(spuId=null, saleAttrId=1, saleAttrName=颜色, saleAttrValues=[SpuSaleAttrValue(spuId=null, saleAttrId=null, saleAttrValueName=黑),
     *                                                                                        SpuSaleAttrValue(spuId=null, saleAttrId=null, saleAttrValueName=白)]),
     *              SpuSaleAttr(spuId=null, saleAttrId=3, saleAttrName=尺码, saleAttrValues=[SpuSaleAttrValue(spuId=null, saleAttrId=null, saleAttrValueName=大),
     *                                                                                      SpuSaleAttrValue(spuId=null, saleAttrId=null, saleAttrValueName=小)])])
     * @param spuInfo
     * @return
     */
    @ResponseBody
    @RequestMapping("/bigSave")
    public String saveAllSpuInfos(@RequestBody SpuInfo spuInfo){
        //log.info("页面提交的spu数据为：{}",spuInfo);
        spuInfoService.saveBigSpuInfo(spuInfo);
        return "ok";
    }


}
