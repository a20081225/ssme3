package com.yw.e3.cart.interceptor;

import com.yw.e3.common.utils.CookieUtils;
import com.yw.e3.common.utils.E3Result;
import com.yw.e3.pojo.TbUser;
import com.yw.e3.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录拦截器
 */
@Controller
public class LoginIntercepetor implements HandlerInterceptor{

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = CookieUtils.getCookieValue(request, "cart", true);
        if (StringUtils.isBlank(token)){//未登录
            return true;
        }
        E3Result e3Result = tokenService.getUserByToken(token);
        if (e3Result.getStatus() != 200){//未取到用户信息
            return true;
        }
        TbUser user = (TbUser) e3Result.getData();//用户信息
        request.setAttribute("user",user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
