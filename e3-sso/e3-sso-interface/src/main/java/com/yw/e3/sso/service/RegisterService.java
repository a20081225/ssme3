package com.yw.e3.sso.service;

import com.yw.e3.common.utils.E3Result;
import com.yw.e3.pojo.TbUser;

public interface RegisterService {
    E3Result checkData(String param,int type);
    E3Result register(TbUser user);
}
