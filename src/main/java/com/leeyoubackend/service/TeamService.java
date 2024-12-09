package com.leeyoubackend.service;

import com.leeyoubackend.pojo.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.leeyoubackend.pojo.Users;
import com.leeyoubackend.pojo.dto.TeamQuery;
import com.leeyoubackend.pojo.request.TeamJoinRequest;
import com.leeyoubackend.pojo.request.TeamQuitRequest;
import com.leeyoubackend.pojo.request.TeamUpdateRequst;
import com.leeyoubackend.pojo.vo.TeamUserVO;

import java.util.List;

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

    /**
     * 搜索队伍
     *
     * @param teamQuery
     * @param loginuser
     * @return
     */
    List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin);

    boolean updateTeam(TeamUpdateRequst teamUpdateRequst,Users loginuser);

    /**
     * 加入队伍
     * @param teamJoinRequst
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequst, Users loginUser);

    boolean quitTeam(TeamQuitRequest teamQuitRequest, Users currentUser);

    boolean deleteTeam(long id, Users currentUser);

    /**
     * 得到用户所有创建的队伍
     * @param loginuser
     * @return
     */
    List<Team> getAllCreatedTeams(Users loginuser);

    List<Team> getAllJoinedTeams(List<Long> idList,Users loginuser);
}
