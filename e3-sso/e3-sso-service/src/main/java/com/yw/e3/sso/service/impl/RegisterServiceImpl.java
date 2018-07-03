package com.yw.e3.sso.service.impl;

import com.yw.e3.common.utils.E3Result;
import com.yw.e3.mapper.TbUserMapper;
import com.yw.e3.pojo.TbUser;
import com.yw.e3.pojo.TbUserExample;
import com.yw.e3.sso.service.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * 用户信息校验Service
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private TbUserMapper userMapper;

    @Override
    public E3Result checkData(String param, int type) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //1:用户名；2:手机号；3:邮箱
        if (type == 1){
            criteria.andUsernameEqualTo(param);
        }else if (type == 2){
            criteria.andPhoneEqualTo(param);
        }else if (type == 3){
            criteria.andEmailEqualTo(param);
        }else {
            return E3Result.build(400,"数据类型错误");
        }
        List<TbUser> list = userMapper.selectByExample(example);
        //判断是否含有用户信息
        if (list != null && list.size()>0){
            return E3Result.ok(false);
        }
        return E3Result.ok(true);
    }

    @Override
    public E3Result register(TbUser user) {
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(user.getPhone())){
            return E3Result.build(400,"用户数据不完整");
        }
        E3Result result = checkData(user.getUsername(), 1);
        if (!(boolean)result.getData()){
            return E3Result.build(400,"此用户已经被占用");
        }
        E3Result result2 = checkData(user.getPhone(), 2);
        if (!(boolean)result2.getData()){
            return E3Result.build(400,"此号码已经被占用");
        }
        user.setCreated(new Date());
        user.setUpdated(new Date());
        String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Password);
        userMapper.insert(user);
        return E3Result.ok();
    }
}
