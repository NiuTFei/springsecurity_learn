package com.xjtu.springsecurity.controller;

import com.xjtu.springsecurity.domain.ResponseResult;
import com.xjtu.springsecurity.domain.User;
import com.xjtu.springsecurity.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

    @Autowired
    private LoginService loginService;

    @RequestMapping("/index")
    @PreAuthorize("hasAuthority('dev:code:push')")
    public String test(){
        return "Hello! Spring Security!";
    }

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){


        return loginService.login(user);

    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){


        return loginService.logout();

    }

}
