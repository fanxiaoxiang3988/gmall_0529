package com.atguigu.gmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.manager.BaseAttrInfo;
import com.atguigu.gmall.manager.BaseAttrInfoService;
import com.atguigu.gmall.manager.BaseAttrValue;
import com.atguigu.gmall.manager.vo.BaseAttrInfoAndValueVO;
import com.atguigu.gmall.manager.vo.BaseAttrValueVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/16 0016
 */
@Slf4j
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

    /**
     * 建vo实体类用来封装页面传来的参数为具体对象（get思路，可以参考并效仿）
     * @param baseAttrInfoAndValueVO
     * BaseAttrInfoAndValueVO(
     * id=1,
     * attrName=励志读物,
     * attrValues=[BaseAttrValueVo(id=1, valueName=名人传, attrId=1),
     *            BaseAttrValueVo(id=2, valueName=励志故事, attrId=1),
     *           BaseAttrValueVo(id=null, valueName=新增测试, attrId=1)])
     * @return
     * @RequestBody 将请求体中的数据封装成指定BaseAttrInfoAndValueVO的对象
     */
    @ResponseBody
    @RequestMapping("/updates")
    public String saveOrUpdateOrDeleteAttrInfoAndValue(@RequestBody BaseAttrInfoAndValueVO baseAttrInfoAndValueVO) {
        log.info("页面所传参数为：{}",baseAttrInfoAndValueVO);
        //判断是修改还是添加
            //修改
            BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
            //将vo中的所有属性复制到bean中
            BeanUtils.copyProperties(baseAttrInfoAndValueVO,baseAttrInfo);
            List<BaseAttrValueVo> attrValues = baseAttrInfoAndValueVO.getAttrValues();
            List<BaseAttrValue> values = new ArrayList<>();
            //baseAttrInfoAndValueVO中的attrValues封装的是BaseAttrValueVo对象，而baseAttrInfo的attrValues封装的是BaseAttrValue，所以需要遍历重新赋值
            for (BaseAttrValueVo attrValue : attrValues) {
                BaseAttrValue baseAttrValue = new BaseAttrValue();
                BeanUtils.copyProperties(attrValue,baseAttrValue);
                values.add(baseAttrValue);
            }
            baseAttrInfo.setAttrValues(values);
            baseAttrInfoService.saveOrUpdateBaseInfo(baseAttrInfo);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/deleteAttrInfoAndValue")
    public String deleteAttrInfoAndValue(Integer id) {
        //log.info("所要删除的平台属性的id为：{}",id);
        //删除平台属性
        int i = baseAttrInfoService.deleteAttrInfoById(id);
        //删除平台属性对应的值
        int j = baseAttrInfoService.deleteAttrValueInfoById(id);

        return "ok";
    }

}
