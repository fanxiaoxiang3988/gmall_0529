package com.atguigu.gmall.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/10 0010
 */

@Data
public class User implements Serializable {

    private String id;

    private String userName;

}
