package com.atguigu.gmall.item.config;

import com.atguigu.gmall.interceptor.LoginRequireInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/9/7 0007
 * LoginRequireInterceptor拦截器配置在gmall-webutil，因为此工程依赖了gmall-webutil，所以可以使用gmall-webutil的内容
 * 但是LoginRequireInterceptor组件所在的包的深度与gmall-item-web启动类加载的深度不同，有可能扫描不到LoginRequireInterceptor组件
 * 有两种解决方式
 * 1、在该类上直接import导入LoginRequireInterceptor组件
 * 2、在gmall-item-web的启动类上标注@ComponentScan("gmall.com.atguigu")放大扫描范围
 */
@Import(LoginRequireInterceptor.class)
@Configuration
public class GmallWebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private LoginRequireInterceptor loginRequireInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        //registry.addInterceptor(new InterceptorDemo2()).addPathPatterns("/**");
        registry.addInterceptor(loginRequireInterceptor)
                .addPathPatterns("/**")//拦截所有的请求
                .excludePathPatterns("*.jpg");//过滤图片
    }

}
