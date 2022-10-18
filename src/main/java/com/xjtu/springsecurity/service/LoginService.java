package com.xjtu.springsecurity.service;

import com.xjtu.springsecurity.domain.ResponseResult;
import com.xjtu.springsecurity.domain.User;

public interface LoginService {

    ResponseResult login(User user);


    ResponseResult logout();
}
