package com.leeyoubackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leeyoubackend.pojo.UserTeam;
import com.leeyoubackend.service.UserTeamService;
import com.leeyoubackend.mapper.UserTeamMapper;
import org.springframework.stereotype.Service;

/**
* @author leeyou
* @description 针对表【user_team(用户队伍关系表)】的数据库操作Service实现
* @createDate 2024-12-04 19:04:56
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService{

}




