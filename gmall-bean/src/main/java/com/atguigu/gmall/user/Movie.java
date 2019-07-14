package com.atguigu.gmall.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/10 0010
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie implements Serializable {

    private String id;

    private String name;

}
