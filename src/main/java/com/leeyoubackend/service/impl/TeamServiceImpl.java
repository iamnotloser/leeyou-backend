package com.leeyoubackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leeyoubackend.constant.ErrorCode;
import com.leeyoubackend.exception.BusinesException;
import com.leeyoubackend.pojo.Team;
import com.leeyoubackend.pojo.UserTeam;
import com.leeyoubackend.pojo.Users;
import com.leeyoubackend.pojo.enums.TeamStausEnum;
import com.leeyoubackend.service.TeamService;
import com.leeyoubackend.mapper.TeamMapper;
import com.leeyoubackend.service.UserTeamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

/**
* @author leeyou
* @description 针对表【team(队伍表)】的数据库操作Service实现
* @createDate 2024-12-04 19:01:49
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{

    @Autowired
    private UserTeamService userTeamService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addTeam(Team team, Users loginuser) {
        final long userId = loginuser.getId();
        if(team == null){
            throw new BusinesException(ErrorCode.NULL_ERROR);
        }
        if(loginuser == null){
            throw new BusinesException(ErrorCode.NOT_LOGIN);
        }
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if(maxNum<1 || maxNum>20){
            throw new BusinesException(ErrorCode.PARAMS_ERROR,"队伍人数超过范围");
        }
        String name = team.getName();
        if(StringUtils.isBlank(name) || name.length()>20){
            throw new BusinesException(ErrorCode.PARAMS_ERROR,"队伍标题不符合要求");
        }
        String description = team.getDescription();
        if(StringUtils.isNotBlank(description) && description.length()>512){
            throw new BusinesException(ErrorCode.PARAMS_ERROR,"队伍描述过长");
        }
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStausEnum statusEnum = TeamStausEnum.getEnumByValue(status);
        if( statusEnum == null){
            throw new BusinesException(ErrorCode.PARAMS_ERROR,"队伍状态不合法");
        }
        String password = team.getPassword();
        if(TeamStausEnum.SECRET.equals(statusEnum) && StringUtils.isBlank(password) || password.length()>32 ){
            throw new BusinesException(ErrorCode.PARAMS_ERROR,"密码设置不正确");
        }
        Date expireTime = Optional.ofNullable(team.getExpireTime()).orElse(new Date());
        if(new Date().after(expireTime)){
            throw new BusinesException(ErrorCode.PARAMS_ERROR,"队伍已过期");
        }
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        long hasTeamNum = this.count(queryWrapper);
        if(hasTeamNum>=5){
            throw new BusinesException(ErrorCode.PARAMS_ERROR,"最多创建5个队伍");
        }
        team.setId(null);
        team.setUserId(userId);

        boolean result = this.save(team);
        Long teamId = team.getId();
        if(true){
            throw new BusinesException(ErrorCode.OPERATION_ERROR,"插入失败");
        }
        if(!result || teamId==null){
            throw new BusinesException(ErrorCode.OPERATION_ERROR,"插入失败");
        }
        UserTeam userTeam = new UserTeam();

        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        result = userTeamService.save(userTeam);
        if(!result){
            throw new BusinesException(ErrorCode.OPERATION_ERROR,"插入失败");
        }
        return teamId;
    }
}




