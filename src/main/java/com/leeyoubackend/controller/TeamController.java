package com.leeyoubackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leeyoubackend.constant.BaseResponse;
import com.leeyoubackend.constant.ErrorCode;
import com.leeyoubackend.exception.BusinesException;
import com.leeyoubackend.pojo.Team;
import com.leeyoubackend.pojo.UserTeam;
import com.leeyoubackend.pojo.Users;
import com.leeyoubackend.pojo.dto.TeamQuery;
import com.leeyoubackend.pojo.request.TeamAddRequst;
import com.leeyoubackend.pojo.request.TeamJoinRequest;
import com.leeyoubackend.pojo.request.TeamQuitRequest;
import com.leeyoubackend.pojo.request.TeamUpdateRequst;
import com.leeyoubackend.pojo.vo.TeamUserVO;
import com.leeyoubackend.service.TeamService;
import com.leeyoubackend.service.UserTeamService;
import com.leeyoubackend.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/team")
@CrossOrigin(value = {"http://localhost:5173/","http://localhost:3000/","http://119.91.48.253:5173/"},allowCredentials = "true")
@Slf4j
public class TeamController {
    @Autowired
    private TeamService teamService;

    @Autowired
    private UserTeamService userTeamService;
    @Autowired
    private UsersService usersService;

    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequst teamAddRequst, HttpServletRequest req){
        if(teamAddRequst == null){
            throw new BusinesException(ErrorCode.NULL_ERROR);
        }
        Team team = new Team();
        Users currentUser = usersService.getCurrentUser(req);
        BeanUtils.copyProperties(teamAddRequst,team);
        long teamId = teamService.addTeam(team,currentUser);
        return BaseResponse.success(teamId);
  }

    @GetMapping ("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestParam long id, HttpServletRequest req){
        if(id <= 0){
            throw new BusinesException(ErrorCode.NULL_ERROR);
        }
        Users currentUser = usersService.getCurrentUser(req);
        boolean result = teamService.deleteTeam(id,currentUser);
        if(!result){
            throw new BusinesException(ErrorCode.OPERATION_ERROR,"删除失败");
        }
        return BaseResponse.success(true);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequst teamUpdateRequst, HttpServletRequest req){
        if(teamUpdateRequst == null){
            throw new BusinesException(ErrorCode.NULL_ERROR);
        }
        Users currentUser = usersService.getCurrentUser(req);
        boolean result = teamService.updateTeam(teamUpdateRequst,currentUser);
        if(!result){
            throw new BusinesException(ErrorCode.OPERATION_ERROR,"更新失败");
        }
        return BaseResponse.success(true);
    }

    @GetMapping("/get")
    public BaseResponse<Team> getTeamById(@RequestParam long id, HttpServletRequest req){
        if(id <= 0){
            throw new BusinesException(ErrorCode.NULL_ERROR);
        }
        Team team = teamService.getById(id);
        if(team == null){
            throw new BusinesException(ErrorCode.NULL_ERROR,"获取失败");
        }
        return BaseResponse.success(team);
    }
//    @GetMapping("/list")
//    public BaseResponse<List<Team>> listTeams(TeamQuery teamQuery){
//        if(teamQuery == null){
//            throw new BusinesException(ErrorCode.NULL_ERROR);
//        }
//        Team team = new Team();
//        try {
//            BeanUtils.copyProperties(teamQuery,team);
//        } catch (BeansException e) {
//            throw new BusinesException(ErrorCode.SYSTEM_ERROR);
//        }
//        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
//        return BaseResponse.success(teamService.list(queryWrapper));
//    }
    @GetMapping("/list")
    public BaseResponse<List<TeamUserVO>> listTeams(TeamQuery teamQuery,HttpServletRequest req) {
        if (teamQuery == null) {
            throw new BusinesException(ErrorCode.NULL_ERROR);
        }
        //1.查询队伍列表信息
        boolean isAdmin = usersService.isAdmin(req);
        List<TeamUserVO> teamList = teamService.listTeams(teamQuery,isAdmin);
        if(CollectionUtils.isEmpty(teamList)){
            return BaseResponse.success(new ArrayList<>());
        }
        //2.查询当前用户是否参加队伍
        List<Long> teamIdList = teamList.stream().map(TeamUserVO::getId).collect(Collectors.toList());
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper();
        try {
            Users currentUser = usersService.getCurrentUser(req);
            queryWrapper.eq("user_id", currentUser.getId());

            queryWrapper.in("team_id", teamIdList);


            List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
            Set<Long> teamHasJoinIdSet = userTeamList.stream().map(UserTeam::getTeamId).collect(Collectors.toSet());
            teamList.forEach(teamUserVO -> {
                boolean hasJoin = teamHasJoinIdSet.contains(teamUserVO.getId());
                teamUserVO.setHasJoin(hasJoin);
            });
        } catch (Exception e) {}
        //查询已加入队伍队员数量
        QueryWrapper<UserTeam> queryJoinWrapper = new QueryWrapper();
        queryJoinWrapper.in("team_id", teamIdList);
        List<UserTeam> userTeamJoinList = userTeamService.list(queryJoinWrapper);
        //队伍id =》 加入该队伍用户列表
        Map<Long, List<UserTeam>> listMap = userTeamJoinList.stream().collect(Collectors.groupingBy(UserTeam::getTeamId));
        teamList.forEach(team -> {

            team.setHasJoinNum(listMap.getOrDefault(team.getId(),new ArrayList<>()).size());
        });
        return BaseResponse.success(teamList);



    }

    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> listTeamsByPage(TeamQuery teamQuery){
        if(teamQuery == null){
            throw new BusinesException(ErrorCode.NULL_ERROR);
        }
        Team team = new Team();
        try {
            BeanUtils.copyProperties(teamQuery,team);
        } catch (BeansException e) {
            throw new BusinesException(ErrorCode.SYSTEM_ERROR);
        }
        Page<Team> page = new Page<>(teamQuery.getPageNum(),teamQuery.getPageSize());
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        return BaseResponse.success(teamService.page(page,queryWrapper));
    }

    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequst, HttpServletRequest req){
        if(teamJoinRequst == null){
            throw new BusinesException(ErrorCode.NULL_ERROR);
        }
        Users currentUser = usersService.getCurrentUser(req);
        boolean result = teamService.joinTeam(teamJoinRequst,currentUser);
        if(!result){
            throw new BusinesException(ErrorCode.OPERATION_ERROR,"加入失败");
        }
        return BaseResponse.success(true);
    }

    @PostMapping("/quit")
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest, HttpServletRequest req){
        if(teamQuitRequest == null){
            throw new BusinesException(ErrorCode.NULL_ERROR);
        }
        Users currentUser = usersService.getCurrentUser(req);
        boolean result = teamService.quitTeam(teamQuitRequest,currentUser);
        if(!result){
            throw new BusinesException(ErrorCode.OPERATION_ERROR,"加入失败");
        }
        return BaseResponse.success(true);
    }

    /**
     * 获取已加入的队伍排除自己创建的队伍
     * @param req
     * @return
     */
    @GetMapping("/list/my/join")
    public BaseResponse<List<Team>> joinTeams(HttpServletRequest req) {
        Users currentUser = usersService.getCurrentUser(req);
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",currentUser.getId());
        List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
        Map<Long, List<UserTeam>> listMap = userTeamList.stream().collect(Collectors.groupingBy(UserTeam::getTeamId));
        List<Long> idlist = new ArrayList<>(listMap.keySet());
        List<Team> teamList = teamService.getAllJoinedTeams(idlist,currentUser);
        return BaseResponse.success(teamList);
    }

    /**
     * 获取我创建的队伍
     * @param req
     * @return
     */
    @GetMapping("/list/my")
    public BaseResponse<List<Team>> createTeams(HttpServletRequest req) {
        Users currentUser = usersService.getCurrentUser(req);
        return BaseResponse.success(teamService.getAllCreatedTeams(currentUser));
    }
}
