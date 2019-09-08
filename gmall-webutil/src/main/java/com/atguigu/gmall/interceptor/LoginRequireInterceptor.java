package com.atguigu.gmall.interceptor;

import com.atguigu.gmall.annotation.LoginRequired;
import com.atguigu.gmall.constant.CookieConstant;
import com.atguigu.gmall.utils.CookieUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

/**
 * springBoot想要写一个拦截器，最简单的做法就是实现HandlerInterceptor接口
 * 返回值：true表示继续流程（如调用下一个拦截器或处理器）；
 *         false表示流程中断（如登录检查失败），不会继续调用其他的拦截器或处理器，此时我们需要通过response来产生响应；
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/9/5 0005
 */
@Component
public class LoginRequireInterceptor implements HandlerInterceptor {

    //目标方法执行之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //先判断这个方法是否需要登陆后才能访问，拿到我们将要执行的目标方法
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Annotation annotation = handlerMethod.getMethodAnnotation(LoginRequired.class);
        if(annotation != null) {
            //标了注解
            //这个token，其实存储的就是令牌，如果有，则证明是刚刚登陆过来，没有的话就根据cookie的值判定是否之前登陆过
            String token = request.getParameter("token");
            String cookieValue = CookieUtils.getCookieValue(request, CookieConstant.SSO_COOKIE_NAME);

            //1、验证是否是第一次过来，只携带了一个token的字符串
            if(!StringUtils.isEmpty(token) && cookieValue == null) {
                //只要有这个token，证明刚刚登陆成功，将这个token设置进cookie中
                Cookie cookie = new Cookie(CookieConstant.SSO_COOKIE_NAME, token);
                cookie.setPath("/");
                response.addCookie(cookie);
                return true;
            }
            //2、验证是否存在登陆的Cookie
            if(!StringUtils.isEmpty(cookieValue)) {
                //说明之前登陆过,Cookie已经放好了
                return true;
            }
            //3、两个都没有，则需要去往登陆页面
            if(StringUtils.isEmpty(token) && StringUtils.isEmpty(cookieValue)) {
                String redirectUrl = "http://www.gmallsso.com/login?originUrl=" + request.getRequestURL();
                response.sendRedirect(redirectUrl);
                return false;
            }
        } else {
            //没有标注解，直接放行
            return true;
        }
        return false;
    }

    //目标方法执行之后
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    //页面渲染出来以后
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
