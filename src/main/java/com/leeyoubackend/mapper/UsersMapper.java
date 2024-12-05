package com.leeyoubackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leeyoubackend.pojo.Users;
import org.apache.ibatis.annotations.Mapper;

/**
* @author leeyou
* @description 针对表【users(用户表)】的数据库操作Mapper
* @createDate 2024-11-23 15:37:21
* @Entity com.usercenter.User.Users
*/
@Mapper
public interface UsersMapper extends BaseMapper<Users> {

}




