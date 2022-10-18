package com.xjtu.springsecurity.handler;

import com.alibaba.fastjson.JSON;
import com.xjtu.springsecurity.domain.ResponseResult;
import com.xjtu.springsecurity.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseResult<Object> responseResult = new ResponseResult<>(HttpStatus.FORBIDDEN.value(), "没有相关权限！");
        String json = JSON.toJSONString(responseResult);
        WebUtils.renderString(response,json);
    }
}
