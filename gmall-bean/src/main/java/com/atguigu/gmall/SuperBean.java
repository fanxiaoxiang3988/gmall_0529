package com.atguigu.gmall;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;

/**
 * 因所有的实体类都有id这一字段且需要实现序列化，所以可以提取出来，之后的实体类只需要集成这一实体类即可
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/16 0016
 */
@Data
public class SuperBean implements Serializable {

    //标识主键
    @TableId(type = IdType.AUTO)//自增主键
    private Integer id;

}

