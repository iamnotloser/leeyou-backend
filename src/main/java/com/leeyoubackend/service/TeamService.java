package com.leeyoubackend.service;

import com.leeyoubackend.pojo.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.leeyoubackend.pojo.Users;

/**
* @author leeyou
* @description 针对表【team(队伍表)】的数据库操作Service
* @createDate 2024-12-04 19:01:49
*/
public interface TeamService extends IService<Team> {

/**
 * 创建队伍
 * @param team
 * @return
 */
    long addTeam(Team team, Users loginuser);
}
