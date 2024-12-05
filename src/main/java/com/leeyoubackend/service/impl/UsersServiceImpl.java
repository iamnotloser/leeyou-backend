package com.leeyoubackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.leeyoubackend.constant.ErrorCode;
import com.leeyoubackend.exception.BusinesException;
import com.leeyoubackend.pojo.Users;
import com.leeyoubackend.service.UsersService;
import com.leeyoubackend.mapper.UsersMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.leeyoubackend.constant.UserConstant.ADMIN_ROLE;
import static com.leeyoubackend.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author leeyou
* @description 针对表【users(用户表)】的数据库操作Service实现
* @createDate 2024-11-23 15:37:21
*/
@Slf4j
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{
    private static final String SALT = "leeyou";


    @Autowired
    private UsersMapper usersMapper;
    @Override
    public long userRegister(String userAccount, String password, String confirmPassword, String planetCode) {
        //1.校验
        if(StringUtils.isAnyBlank(userAccount,password,confirmPassword,planetCode)){
            throw new BusinesException(ErrorCode.PARAMS_ERROR);
        }
        if(userAccount.length()<4){
            throw new BusinesException(ErrorCode.PARAMS_ERROR);
        }
        if(password.length()<8||confirmPassword.length()<8){
            throw new BusinesException(ErrorCode.PARAMS_ERROR);
        }

        //校验特殊字符

        String regEx = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(regEx).matcher(userAccount);
        if(matcher.find()){
            throw new BusinesException(ErrorCode.PARAMS_ERROR);
        }
        //密码和确认密码是否一致
        if(!password.equals(confirmPassword)){
            throw new BusinesException(ErrorCode.PARAMS_ERROR);
        }
        //账户不能重复
        QueryWrapper<Users> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        long result = this.count(queryWrapper);
        if(result>0){
            throw new BusinesException(ErrorCode.PARAMS_ERROR,"输入的账户名称不能重复");
        }
        //星球不能重复
        queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("planet_code",planetCode);
        long newresult = this.count(queryWrapper);
        if(newresult>0){
            throw new BusinesException(ErrorCode.PARAMS_ERROR,"输入的星球号码不能重复");
        }
        //2.加密

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        //3.插入数据
        Users users = new Users();
        users.setUserAccount(userAccount);
        users.setPassword(encryptPassword);
        users.setPlanetCode(planetCode);
        boolean saveResult = this.save(users);
        if(!saveResult){
            return -1;
        }
        return users.getId();
    }

    @Override
    public Users userLogin(String userAccount, String password, HttpServletRequest req) {
        //1.校验
        if(StringUtils.isAnyBlank(userAccount,password)){
            throw new BusinesException(ErrorCode.PARAMS_ERROR,"信息为空");
        }
        if(userAccount.length()<4){
            throw new BusinesException(ErrorCode.PARAMS_ERROR,"账户长度小于4");
        }
        if(password.length()<8){
            return null;
        }

        //校验特殊字符

        String regEx = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(regEx).matcher(userAccount);
        if(matcher.find()){
            throw new BusinesException(ErrorCode.PARAMS_ERROR,"有特殊字符");
        }

        //2.加密

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        //3.查询用户是否存在
        QueryWrapper<Users> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        queryWrapper.eq("password",encryptPassword);
        Users users = this.getOne(queryWrapper);
        if(users==null){
            log.info("用户不存在");
            throw new BusinesException(ErrorCode.PARAMS_ERROR,"用户不存在");
        }
        //3.用户脱敏
        Users safeUser = getsaferUser(users);
        //4.记录用户登录态
        req.getSession().setAttribute(USER_LOGIN_STATE,safeUser);


        return safeUser;
    }

    @Override
    public List<Users> searchUsers(String username) {
        QueryWrapper<Users> queryWrapper=new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
        List<Users> users = usersMapper.selectList(queryWrapper);
        return users.stream().map(this::getsaferUser).collect(Collectors.toList());
    }
    @Override
    public Users getsaferUser(Users originUser){
        if(originUser==null) {
            return null;
        }
        Users safeUser = new Users();
        safeUser.setId(originUser.getId());
        safeUser.setUsername(originUser.getUsername());
        safeUser.setUserAccount(originUser.getUserAccount());

        safeUser.setAvatarUrl(originUser.getAvatarUrl());
        safeUser.setGender(originUser.getGender());
        safeUser.setPhone(originUser.getPhone());
        safeUser.setEmail(originUser.getEmail());
        safeUser.setStatus(originUser.getStatus());
        safeUser.setCreateTime(originUser.getCreateTime());
        safeUser.setRole(originUser.getRole());
        safeUser.setPlanetCode(originUser.getPlanetCode());
        safeUser.setTags(originUser.getTags());
        return safeUser;
    }

    @Override
    public int userLogout(HttpServletRequest req) {
        req.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;

    }

    /**
     * 根据标签搜索用户
     *
     * @param taglist
     * @return
     */

    @Override
    public List<Users> searchUsersByTags(List<String> taglist){
        //2/内存查询
        if(CollectionUtils.isEmpty(taglist)){
            throw new BusinesException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper queryWrapper=new QueryWrapper<>();
        List<Users> usersList = usersMapper.selectList(queryWrapper);
        Gson gson = new Gson();
        return usersList.stream().filter(user -> {
            String tags = user.getTags();
            if(StringUtils.isBlank(tags)){
                return false;
            }
            Set<String> list = gson.fromJson(tags, Set.class);
            for(String tag:taglist){
                if(!list.contains(tag)){
                    return false ;
                }
            }
            return true;
        }).map((this::getsaferUser)).collect(Collectors.toList());
    }

    @Override
    public Integer updateUser(Users user, Users loginuser) {
        //仅管理员和自己可以修改
        //如果是管理员允许更新任意用户
        long id = user.getId();
        if(id<=0){
            throw new BusinesException(ErrorCode.PARAMS_ERROR);
        }
        if(!isAdmin(loginuser)&&id != loginuser.getId()){
            throw new BusinesException(ErrorCode.NO_AUTH);
        }
        Users oldUser = usersMapper.selectById(id);
        if(oldUser==null){
            throw new BusinesException(ErrorCode.NULL_ERROR);
        }
        return usersMapper.updateById(user);
    }

    @Override
    public Users getCurrentUser(HttpServletRequest req) {
        if(req==null){
            throw new BusinesException(ErrorCode.NOT_LOGIN);
        }
        Object userobject = req.getSession().getAttribute(USER_LOGIN_STATE);
        Users loginuser = (Users)userobject;
        if(loginuser==null){
            throw new BusinesException(ErrorCode.NOT_LOGIN);
        }
        return loginuser;
    }

    @Override
    public Boolean isAdmin(HttpServletRequest request) {
        Object userobject = request.getSession().getAttribute(USER_LOGIN_STATE);
        Users user = (Users)userobject;
        System.out.println(user.getRole());
        return user != null && Objects.equals(user.getRole(), ADMIN_ROLE);
    }
    @Override
    public Boolean isAdmin(Users loginuser) {

        return loginuser != null && Objects.equals(loginuser.getRole(), ADMIN_ROLE);
    }


    @Deprecated
    private List<Users> searchUsersByTagsSQL(List<String> taglist){
        if(CollectionUtils.isEmpty(taglist)){
            throw new BusinesException(ErrorCode.PARAMS_ERROR);
        }
        //1.sql查询
        QueryWrapper queryWrapper=new QueryWrapper<>();
        for (String tag:taglist){
            queryWrapper = (QueryWrapper) queryWrapper.like("tags",tag);
        }
        List<Users> usersList = usersMapper.selectList(queryWrapper);
        return usersList.stream().map((this::getsaferUser)).collect(Collectors.toList());

    }

}




