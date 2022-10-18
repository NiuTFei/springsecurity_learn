package com.xjtu.springsecurity.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xjtu.springsecurity.domain.LoginUser;
import com.xjtu.springsecurity.domain.ResponseResult;
import com.xjtu.springsecurity.domain.User;
import com.xjtu.springsecurity.service.LoginService;
import com.xjtu.springsecurity.utils.JwtUtil;
import com.xjtu.springsecurity.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {

        //使用ProviderManager.authentic()进行验证

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误！");
        }

        //生成jwt给前端
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);

        Map<String ,String> map = new HashMap<>();
        map.put("token",jwt);

        //系统用户信息存入redis
        redisCache.setCacheObject("login:" + userId,loginUser);

        return new ResponseResult<>(200,"登陆成功！",map);
    }

    @Override
    public ResponseResult logout() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        JSONObject principal = (JSONObject) authentication.getPrincipal();
//        LoginUser loginUser = JSONObject.toJavaObject(principal, LoginUser.class);
        redisCache.deleteObject("login:" + loginUser.getUser().getId());

        return new ResponseResult<>(200,"退出成功！");
    }
}
