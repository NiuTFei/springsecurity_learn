package com.xjtu.springsecurity.filter;

import com.alibaba.fastjson.JSONObject;
import com.xjtu.springsecurity.domain.LoginUser;
import com.xjtu.springsecurity.utils.JwtUtil;
import com.xjtu.springsecurity.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1获取header中的token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)){
            //放行，让后续的过滤器执行
            filterChain.doFilter(request,response);
            return;
        }

        String userId;
        //2解析token,得到userId
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //3根据userId，从redis中得到用户信息
        JSONObject object = redisCache.getCacheObject("login:" + userId);
        LoginUser loginUser = JSONObject.toJavaObject(object, LoginUser.class);
        if (Objects.isNull(loginUser)){
//            throw new RuntimeException("未登录！");
            System.out.println("未登录");
            return;
        }

        //4封装Authentication
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());

        //5存入SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request,response);
    }
}
