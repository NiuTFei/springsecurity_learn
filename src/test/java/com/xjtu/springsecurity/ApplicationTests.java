package com.xjtu.springsecurity;

import com.xjtu.springsecurity.mapper.MenuMapper;
import com.xjtu.springsecurity.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.web.util.WebUtils;

import java.util.List;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MenuMapper mapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        String root = passwordEncoder.encode("fei990628");
        System.out.println(root);
    }


    @Test
    void testSelectPerms(){

        List<String> list = mapper.selectPermsByUserId(4L);
        System.out.println(list);

    }

    @Test
    void testSelectRoles(){
        List<String> list = userMapper.selectRolesByUserId(3L);
        System.out.println(list);
    }

}
