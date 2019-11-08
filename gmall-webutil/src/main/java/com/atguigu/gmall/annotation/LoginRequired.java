package com.atguigu.gmall.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;


/**
 * 这个注解是标注在方法上的，运行时有效
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/9/5 0005
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {

    boolean needLogin() default true;

}
