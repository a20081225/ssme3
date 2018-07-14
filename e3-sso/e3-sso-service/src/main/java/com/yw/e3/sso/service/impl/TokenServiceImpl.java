package com.yw.e3.sso.service.impl;

import com.yw.e3.common.jedis.JedisClient;
import com.yw.e3.common.utils.E3Result;
import com.yw.e3.common.utils.JsonUtils;
import com.yw.e3.pojo.TbUser;
import com.yw.e3.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * 根据token获取用户信息
 */
@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private JedisClient jedisClient;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;
    @Override
    public E3Result getUserByToken(String token) {
        String json = jedisClient.get("session:" + token);
        if (StringUtils.isBlank(json)){
            return E3Result.build(400,"用户登录过期");
        }
        jedisClient.expire("session:" + token,SESSION_EXPIRE);
        TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
        return E3Result.ok(user);
    }
}
