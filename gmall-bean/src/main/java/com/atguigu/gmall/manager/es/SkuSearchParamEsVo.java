package com.atguigu.gmall.manager.es;

import lombok.Data;
import java.io.Serializable;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/8/7 0007
 */
@Data
public class SkuSearchParamEsVo implements Serializable {

    private String keyword; //关键字搜索
    private Integer catalog3Id; //三级目录id  catalog3Id
    private Integer[] valueId; //这个值会有多个
    private Integer pageNo = 1;  //页码
    private Integer pageSize = 12;
    private String sortField = "hotScore";
    private String sortOrder = "desc";

}
