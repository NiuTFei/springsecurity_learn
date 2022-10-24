package com.xjtu.springsecurity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjtu.springsecurity.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<String> selectRolesByUserId(Long userId);
}