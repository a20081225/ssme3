package com.yw.e3.sso.controller;

import com.yw.e3.common.utils.E3Result;
import com.yw.e3.pojo.TbUser;
import com.yw.e3.sso.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 注册用户
 */
@Controller
public class RegisterContoller {

    @Autowired
    private RegisterService registerService;

    @RequestMapping("/page/register")
    public String showRegister(){
        return "register";
    }

    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public E3Result checkData(@PathVariable String param,@PathVariable int type){
        E3Result e3Result = registerService.checkData(param, type);
        return  e3Result;
    }

    @RequestMapping(value = "/user/register",method = RequestMethod.POST)
    @ResponseBody
    public E3Result register(TbUser user){
        E3Result e3Result = registerService.register(user);
        return  e3Result;
    }


}
