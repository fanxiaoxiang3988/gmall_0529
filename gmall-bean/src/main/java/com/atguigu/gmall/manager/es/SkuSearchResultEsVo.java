package com.atguigu.gmall.manager.es;

import lombok.Data;
import java.util.List;
import java.io.Serializable;
import com.atguigu.gmall.manager.BaseAttrInfo;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/8/7 0007
 */
@Data
public class SkuSearchResultEsVo implements Serializable {

    private List<SkuInfoEsVo> skuInfoEsVos;//es中查到的skuInfo的所有信息

    //分页条上该显示的内容
    private Integer total;//总记录数
    private Integer pageNo;//第几页

    //告诉页面可供筛选的所有平台属性的名和平台属性对应的每一个值
    private List<BaseAttrInfo> baseAttrInfos;

}
