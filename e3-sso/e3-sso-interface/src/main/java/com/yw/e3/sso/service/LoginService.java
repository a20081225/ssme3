package com.yw.e3.sso.service;

import com.yw.e3.common.utils.E3Result;

public interface LoginService {
    E3Result userLogin(String username,String password);
}
