package com.xjtu.springsecurity.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xjtu.springsecurity.domain.LoginUser;
import com.xjtu.springsecurity.domain.ResponseResult;
import com.xjtu.springsecurity.domain.User;
import com.xjtu.springsecurity.mapper.MenuMapper;
import com.xjtu.springsecurity.mapper.UserMapper;
import com.xjtu.springsecurity.service.LoginService;
import com.xjtu.springsecurity.utils.JwtUtil;
import com.xjtu.springsecurity.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {

        //使用ProviderManager.authentic()进行验证

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
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

        return new ResponseResult<>(20000,"登陆成功！",map);
    }

    @Override
    public ResponseResult logout() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        JSONObject principal = (JSONObject) authentication.getPrincipal();
//        LoginUser loginUser = JSONObject.toJavaObject(principal, LoginUser.class);
        redisCache.deleteObject("login:" + loginUser.getUser().getId());

        return new ResponseResult<>(20000,"退出成功！","退出成功！");
    }

    @Override
    public ResponseResult info(String token) {

        String userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long id = Long.parseLong(userId);


        User user = userMapper.selectById(id);
        List<String> roles = userMapper.selectRolesByUserId(id);//查询得到的角色
        List<String> perms = menuMapper.selectPermsByUserId(id);//查询得到的权限


        LoginUser loginUser = new LoginUser(user,perms,roles);

        return new ResponseResult<>(20000,loginUser);
    }
}
