package com.atguigu.gmall.interceptor;

import com.atguigu.gmall.annotation.LoginRequired;
import com.atguigu.gmall.constant.CookieConstant;
import com.atguigu.gmall.utils.CookieUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * springBoot想要写一个拦截器，最简单的做法就是实现HandlerInterceptor接口
 * 注意：此时拦截器只是写好了，但是并没有实际配置在项目中，如果想要在项目启动时使用，需要设置好拦截路径等内容
 * 返回值：true表示继续流程，就是放行的意思（如调用下一个拦截器或处理器）；
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
        LoginRequired annotation = handlerMethod.getMethodAnnotation(LoginRequired.class);
        if(annotation != null) {
            //标了注解
            //这个token，其实存储的就是令牌，如果有，则证明是刚刚登陆过来，没有的话就根据cookie的值判定是否之前登陆过
            String token = request.getParameter("token");
            String cookieValue = CookieUtils.getCookieValue(request, CookieConstant.SSO_COOKIE_NAME);
            //获取参数，看这个方法是否一定需要用户进行登陆
            boolean needLogin = annotation.needLogin();

            //1、验证是否是第一次过来，只携带了一个token的字符串并且cookie中没有值
            if(!StringUtils.isEmpty(token) && cookieValue == null) {
                //只要有这个token，证明刚刚登陆成功，将这个token设置进cookie中
                Cookie cookie = new Cookie(CookieConstant.SSO_COOKIE_NAME, token);
                cookie.setPath("/");
                /**
                 * 之前，每一次登陆在没有设置domain参数的时候，cookie的domain参数值都是search.gmall.com这种类型，getCookieValue得到为null，所以无法共享
                 * 所以就会导致，当我们点击添加购物车的时候，即使已经处于登陆状态，但是方法到了判断needLogin时，因为是false直接放行，所以仍然无法添加用户购物车
                 * 但是设置domain参数了以后，domain的值为gmall.com，getCookieValue可以跨域取值，可以共享，就不需要重定向去psaaportWeb进行验证了
                 * 更完美的实现了，同域系统下，一处登陆，处处登陆
                 */
                cookie.setDomain("gmall.com");
                response.addCookie(cookie);
                //将用户的信息也放上
                Map<String, Object> map = CookieUtils.resolveTokenData(token);
                //解好以后放进请求域中，当次请求就可以使用了
                request.setAttribute(CookieConstant.LOGIN_USER_INFO_KEY, map);
                return true;
            }
            //2、验证是否存在登陆的Cookie
            if(!StringUtils.isEmpty(cookieValue)) {
                //说明之前登陆过,Cookie已经放好了，需要验证令牌的准确性以及时效性
                try {
                    RestTemplate restTemplate = new RestTemplate();
                    String confirmTokenUrl = "http://www.gmallsso.com/confirmToken?token="+cookieValue;
                    String result = restTemplate.getForObject(confirmTokenUrl, String.class);
                    System.out.println("远程验证的结果是：" + result);
                    if("ok".equals(result)){
                        //验证成功
                        //解析token，将封装的用户信息放入请求域中
                        Map<String, Object> map = CookieUtils.resolveTokenData(cookieValue);
                        //解好以后放进请求域中，当次请求就可以使用了
                        request.setAttribute(CookieConstant.LOGIN_USER_INFO_KEY, map);
                        return true;
                    }else{
                        //验证失败，重新去登陆
                        if(needLogin ==  true) {
                            String redirectUrl = "http://www.gmallsso.com/login?originUrl=" + request.getRequestURL();
                            response.sendRedirect(redirectUrl);
                            return false;
                        }
                        return true;
                    }
                } catch (Exception e){
                    //远程服务器都连不上目标方法不执行
                    return false;
                }
            }
            //3、两个都没有，则需要去往登陆页面
            if(StringUtils.isEmpty(token) && StringUtils.isEmpty(cookieValue)) {
                if(needLogin == true) {
                    String redirectUrl = "http://www.gmallsso.com/login?originUrl=" + request.getRequestURL();
                    response.sendRedirect(redirectUrl);
                    return false;
                }
                return true;
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
