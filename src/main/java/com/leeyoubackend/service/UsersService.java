package com.leeyoubackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leeyoubackend.constant.BaseResponse;
import com.leeyoubackend.pojo.Users;
import com.leeyoubackend.pojo.vo.TeamUserVO;
import com.leeyoubackend.pojo.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

import static com.leeyoubackend.constant.UserConstant.ADMIN_ROLE;
import static com.leeyoubackend.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author leeyou
* @description 针对表【users(用户表)】的数据库操作Service
* @createDate 2024-11-23 15:37:21
*/
public interface UsersService extends IService<Users> {

    /**
     * 用户注册
     * @param userAccount 用户账户
     * @param password 密码
     * @param confirmPassword 确认密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String password, String confirmPassword, String planetCode);
    /**
     * 用户登录
     * @param userAccount 用户账户
     * @param password 密码
     *                 req
     * @return 脱敏后的用户信息
     */
    Users userLogin(String userAccount, String password, HttpServletRequest req);

    List<Users> searchUsers(String username);

    Users getsaferUser(Users originUser);

    int userLogout(HttpServletRequest req);

    List<Users> searchUsersByTags(List<String> taglist);

    Integer updateUser(Users user, Users loginuser);

    Users getCurrentUser(HttpServletRequest req);

    Boolean isAdmin(HttpServletRequest request);

    Boolean isAdmin(Users loginuser);


    List<Users> matchUsers(long num, HttpServletRequest req);
}
