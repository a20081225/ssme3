package com.yw.e3.sso.controller;

import com.yw.e3.common.utils.CookieUtils;
import com.yw.e3.common.utils.E3Result;
import com.yw.e3.sso.service.LoginService;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录
 */
@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    @RequestMapping("/page/login")
    public String showLogin(){
        return "login";
    }

    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    @ResponseBody
    public E3Result register(String username, String password, HttpServletRequest request, HttpServletResponse response){
        E3Result e3Result = loginService.userLogin(username,password);
        if (e3Result.getStatus() == 200){
            String token = e3Result.getData().toString();
            CookieUtils.setCookie(request,response,TOKEN_KEY,token);
        }
        return  e3Result;
    }


}
