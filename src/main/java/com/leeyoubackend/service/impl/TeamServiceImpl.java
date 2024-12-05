package com.leeyoubackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leeyoubackend.constant.ErrorCode;
import com.leeyoubackend.exception.BusinesException;
import com.leeyoubackend.pojo.Team;
import com.leeyoubackend.pojo.UserTeam;
import com.leeyoubackend.pojo.Users;
import com.leeyoubackend.pojo.dto.TeamQuery;
import com.leeyoubackend.pojo.enums.TeamStausEnum;
import com.leeyoubackend.pojo.request.TeamJoinRequst;
import com.leeyoubackend.pojo.request.TeamUpdateRequst;
import com.leeyoubackend.pojo.vo.TeamUserVO;
import com.leeyoubackend.pojo.vo.UserVO;
import com.leeyoubackend.service.TeamService;
import com.leeyoubackend.mapper.TeamMapper;
import com.leeyoubackend.service.UserTeamService;
import com.leeyoubackend.service.UsersService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @Autowired
    private UsersService usersService;
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

    @Override
    public List<TeamUserVO> listTeams(TeamQuery teamQuery, Boolean isAdmin) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        if(teamQuery != null){
            Long id = teamQuery.getId();
            if(id != null && id > 0){
                queryWrapper.eq("id",id);
            }
            String searchText = teamQuery.getSearchText();
            String description = teamQuery.getDescription();
            if(StringUtils.isNotBlank(searchText)){
                queryWrapper.and(qw -> qw.like("name",searchText).or().like("description",description));
            }
            String name = teamQuery.getName();
            if(StringUtils.isNotBlank(name)){
                queryWrapper.like("name",name);
            }
            /**
             * 根据描述查询
             */

            if(StringUtils.isNotBlank(description)){
                queryWrapper.like("description",description);
            }
            Integer maxNum = teamQuery.getMaxNum();
            //查询最大人数相等
            if(maxNum != null && maxNum > 0){
                queryWrapper.eq("max_num",maxNum);
            }
            Long userId = teamQuery.getUserId();
            //根据创建人来查询
            if(userId != null && userId > 0){
                queryWrapper.eq("user_id",userId);
            }
            /**
             * 根据状态查询
             */
            Integer status = teamQuery.getStatus();
            TeamStausEnum statusEnum = TeamStausEnum.getEnumByValue(status);
            if(statusEnum == null){
                statusEnum = TeamStausEnum.PUBLIC;
            }
            if(!isAdmin && statusEnum.equals(TeamStausEnum.PRIVATE)){
                throw new BusinesException(ErrorCode.NO_AUTH);
            }

            queryWrapper.eq("status",statusEnum.getValue());
        }
        //不展示已过期的队伍
        //select * from team where expire_time > now()

        queryWrapper.and(qw ->qw.gt("expire_time",new Date()).or().isNull("expire_time"));

        List<Team> teamList = this.list(queryWrapper);
        if(CollectionUtils.isEmpty(teamList)){
            return new ArrayList<>();
        }

//      关联查询用户信息
        //查询队伍和创建人的信息
        // select * from team t left join user_leeyou u on t.user_id = u.id
        //查询队伍和已加入队员的信息
        //select *
        //from team t
        //         left join user_team ut on t.id = ut.team_id
        //         left join user_leeyou u on ut.user_id = u.id;
        List<TeamUserVO> teamUserVOList = new ArrayList<>();
//        Team team = new Team();
//        BeanUtils.copyProperties(teamQuery,team);
        for(Team team :teamList){
            Long userId = team.getUserId();
            if(userId == null){
                continue;
            }
            Users user = usersService.getById(userId);
            //脱敏
            TeamUserVO teamUserVO = new TeamUserVO();
            BeanUtils.copyProperties(team,teamUserVO);

            if(user != null){
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(user,userVO);
                teamUserVO.setCreateUser(userVO);

            }
            teamUserVOList.add(teamUserVO);

        }
        return teamUserVOList;
    }

    /**
     * 更新队伍
     * @param teamUpdateRequst
     * @return
     */
    @Override
    public boolean updateTeam(TeamUpdateRequst teamUpdateRequst,Users loginUser) {
        if(teamUpdateRequst == null){
            throw new BusinesException(ErrorCode.NULL_ERROR);
        }
        Long id = teamUpdateRequst.getId();
        if(id == null || id <= 0){
            throw new BusinesException(ErrorCode.PARAMS_ERROR);
        }
        Team oldTeam = this.getById(id);
        if(oldTeam == null){
            throw new BusinesException(ErrorCode.NULL_ERROR,"队伍不存在");
        }
        if(loginUser.getId() != oldTeam.getUserId() && !usersService.isAdmin(loginUser)){
            throw new BusinesException(ErrorCode.NO_AUTH,"无操作权限");
        }
        //判断加密状态一定要有密码
        if(TeamStausEnum.SECRET.equals(TeamStausEnum.getEnumByValue(teamUpdateRequst.getStatus()))){
            if(StringUtils.isBlank(teamUpdateRequst.getPassword()) && StringUtils.isBlank(oldTeam.getPassword())){
                throw new BusinesException(ErrorCode.PARAMS_ERROR,"加密房间必须设置密码");
            }
        }
        Team team = new Team();
        BeanUtils.copyProperties(teamUpdateRequst,team);
        boolean updateresult = this.updateById(team);
        return updateresult;
    }

    @Override
    public boolean joinTeam(TeamJoinRequst teamJoinRequst,Users loginUser) {
        if(teamJoinRequst == null){
            throw new BusinesException(ErrorCode.NULL_ERROR);
        }
        long userid = loginUser.getId();

        Long teamId = teamJoinRequst.getTeamId();
        if(teamId == null || teamId <= 0){
            throw new BusinesException(ErrorCode.PARAMS_ERROR);
        }
        Team team = this.getById(teamId);
        if(team == null){
            throw new BusinesException(ErrorCode.NULL_ERROR,"队伍不存在");
        }
//        if(team.getUserId() == userid){
//            throw new BusinesException(ErrorCode.PARAMS_ERROR,"不能加入自己的队伍");
//        }
        Date expireTime = team.getExpireTime();
        if( expireTime != null && expireTime.before(new Date())){
            throw new BusinesException(ErrorCode.PARAMS_ERROR,"队伍已过期");
        }

        Integer status = team.getStatus();
        TeamStausEnum enumByValue = TeamStausEnum.getEnumByValue(status);
        if(TeamStausEnum.PRIVATE.equals(enumByValue)){
            throw new BusinesException(ErrorCode.PARAMS_ERROR,"禁止加入私有队伍");
        }
        if(TeamStausEnum.SECRET.equals(enumByValue)){
            String password = teamJoinRequst.getPassword();
            if(StringUtils.isBlank(password) || !password.equals(team.getPassword())){
                throw new BusinesException(ErrorCode.PARAMS_ERROR,"密码错误");
            }
        }
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userid);
        long hasJoinNum = userTeamService.count(queryWrapper);
        if(hasJoinNum > 5){
            throw new BusinesException(ErrorCode.PARAMS_ERROR,"最多创建和加入5个队伍");
        }
        //不能加入已加入的队伍
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userid);
        queryWrapper.eq("team_id",teamId);
        long hasUserJoinTeam = userTeamService.count(queryWrapper);
        if(hasUserJoinTeam > 0){
            throw new BusinesException(ErrorCode.PARAMS_ERROR,"已加入该队伍");
        }
        //判断队伍人数是否超过
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id",teamId);
        long hasJoinUserNum = userTeamService.count(queryWrapper);
        if(team.getMaxNum() <= hasJoinUserNum){
            throw new BusinesException(ErrorCode.PARAMS_ERROR,"队伍已满");
        }
        //修改队伍信息
        UserTeam userTeam = new UserTeam();

        userTeam.setUserId(userid);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        boolean result = userTeamService.save(userTeam);
        return result;
    }
}




