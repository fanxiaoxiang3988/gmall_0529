package com.atguigu.gmall.manager.controller;

import java.util.List;

import com.atguigu.gmall.manager.SkuEsService;
import lombok.extern.slf4j.Slf4j;
import com.atguigu.gmall.manager.SkuService;
import com.atguigu.gmall.manager.sku.SkuInfo;
import com.atguigu.gmall.manager.spu.SpuImage;
import com.atguigu.gmall.manager.BaseAttrInfo;
import com.atguigu.gmall.manager.SpuInfoService;
import com.atguigu.gmall.manager.spu.SpuSaleAttr;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/25 0025
 */
@Slf4j
@RestController
@RequestMapping("/sku")
public class SkuController {

    @Reference
    private SkuService skuService;
    @Reference
    private SpuInfoService spuInfoService;
    @Reference
    private SkuEsService skuEsService;

    /**
     * 根据3级分类id查出其对应的所有的平台属性名及其属性值
     * @param catalog3Id
     * @return
     */
    @RequestMapping("/base_attr_info.json")
    public List<BaseAttrInfo> getBaseAttrInfoByCatalog3Id(@RequestParam("id") Integer catalog3Id) {
        return skuService.getBaseAttrInfoByCatalog3Id(catalog3Id);
    }

    /**
     * 根据当前页面选中行的spu的id查出其所有的可供选择的销售属性名及其对应的值
     * @param spuId
     * @return
     */
    @RequestMapping("/spu_sale_attr.json")
    public List<SpuSaleAttr> getSpuSaleAttrBySpuId(@RequestParam("id") Integer spuId){
        return skuService.getSpuSaleAttrBySpuId(spuId);
    }

    /**
     * 根据页面所选择的spu，查到该spu下所有的图片，以供sku去选择
     * @param spuId
     * @return
     */
    @RequestMapping("/spuImgaes")
    public List<SpuImage> getSpuImages(@RequestParam("id") Integer spuId) {
        return spuInfoService.getSpuImages(spuId);
    }

    /**
     * 为某个指定的spu增加一条sku商品信息
     * @param skuInfo
     * @return
     * 页面提交过来的大skuInfo信息：
     * SkuInfo
     * (spuId=55, price=3988.00, skuName=小米8青春版, skuDesc=小米8青春版，你值得拥有, weight=1.00, tmId=null, catalog3Id=61, skuDefaultImg=http://file.gmall.com/group1/M00/00/00/wKjYgF05yA-ATMjQAABwM7Yaww0302.jpg,
     * skuImages=[SkuImage(skuId=null, imgName=黑背.jpg, imgUrl=http://file.gmall.com/group1/M00/00/00/wKjYgF05yA-ATl1JAAAnqwz0YWg594.jpg, spuImgId=199, isDefault=0),
     *          SkuImage(skuId=null, imgName=黑侧.jpg, imgUrl=http://file.gmall.com/group1/M00/00/00/wKjYgF05yA-AMFpbAABp1xFAhrU868.jpg, spuImgId=200, isDefault=0),
     *          SkuImage(skuId=null, imgName=黑全.jpg, imgUrl=http://file.gmall.com/group1/M00/00/00/wKjYgF05yA-ATMjQAABwM7Yaww0302.jpg, spuImgId=201, isDefault=1),
     *          SkuImage(skuId=null, imgName=黑正.jpg, imgUrl=http://file.gmall.com/group1/M00/00/00/wKjYgF05yA-AMhOmAABv-lCqbtw539.jpg, spuImgId=202, isDefault=0),
     *          SkuImage(skuId=null, imgName=黑正背.jpg, imgUrl=http://file.gmall.com/group1/M00/00/00/wKjYgF05yA-AGAQAAACDYa0I2wg038.jpg, spuImgId=203, isDefault=0)],
     * skuAttrValues=[SkuAttrValue(attrId=23, valueId=83, skuId=null),
     *               SkuAttrValue(attrId=24, valueId=82, skuId=null)],
     * skuSaleAttrValues=[SkuSaleAttrValue(skuId=null, saleAttrId=null, saleAttrName=尺码, saleAttrValueId=121, saleAttrValueName=超级大),
     *                  SkuSaleAttrValue(skuId=null, saleAttrId=null, saleAttrName=版本, saleAttrValueId=120, saleAttrValueName=美人版),
     *                  SkuSaleAttrValue(skuId=null, saleAttrId=null, saleAttrName=颜色, saleAttrValueId=117, saleAttrValueName=深空土灰)])
     */
    @RequestMapping("/bigsave")
    public String skuBigSave(@RequestBody SkuInfo skuInfo){
        log.info("页面提交过来的大skuInfo信息：{}",skuInfo);
        skuService.saveBigSkuInfo(skuInfo);
        return "OK";
    }

    @RequestMapping("/skuinfo")
    public List<SkuInfo> getSkuInfoBySpuId(@RequestParam("id") Integer spuId){
        return skuService.getSkuInfoBySpuId(spuId);
    }

    /**
     * 根据传入的skuId上架商品，将其缓存进es中
     * 首页（http://search.gmall.com/list.html）获取es中的数据
     * 只有缓存进es才可以查看和购买
     * @return
     */
    @RequestMapping("/onSale")
    public String onSale(@RequestParam("skuId") Integer skuId) {
        //这个方法最好是一个异步方法，不要阻塞其他请求了
        skuEsService.onSale(skuId);
        return "ok";
    }

}
