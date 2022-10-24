package com.xjtu.springsecurity.controller;

import com.xjtu.springsecurity.domain.ResponseResult;
import com.xjtu.springsecurity.domain.User;
import com.xjtu.springsecurity.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        System.out.println("正在登陆");

        return loginService.login(user);
    }

    @RequestMapping("/user/info")
    public ResponseResult info(String token){

        return loginService.info(token);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){


        return loginService.logout();

    }

}
