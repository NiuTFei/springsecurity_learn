package com.xjtu.springsecurity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xjtu.springsecurity.domain.LoginUser;
import com.xjtu.springsecurity.domain.User;
import com.xjtu.springsecurity.mapper.MenuMapper;
import com.xjtu.springsecurity.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名获取数据库中的用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(wrapper);
        if (Objects.isNull(user)){
            throw new RuntimeException("该用户不存在");
        }
        //TODO 权限查询

        List<String> permissions = menuMapper.selectPermsByUserId(user.getId());

        //返回
        return new LoginUser(user,permissions);
    }
}
