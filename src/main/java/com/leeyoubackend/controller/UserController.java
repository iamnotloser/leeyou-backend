package com.leeyoubackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leeyoubackend.constant.BaseResponse;
import com.leeyoubackend.constant.ErrorCode;
import com.leeyoubackend.exception.BusinesException;
import com.leeyoubackend.pojo.Users;
import com.leeyoubackend.pojo.request.UserLoginRequest;
import com.leeyoubackend.pojo.request.UserRegisterRequest;
import com.leeyoubackend.pojo.vo.UserVO;
import com.leeyoubackend.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.leeyoubackend.constant.UserConstant.USER_LOGIN_STATE;
//@Api(tags = "测试")
@RestController
@RequestMapping("/user")
@CrossOrigin(value = {"http://localhost:5173/","http://localhost:3000/","http://119.91.48.253:5173/"},allowCredentials = "true")
@Slf4j
public class UserController {
    @Autowired
    private UsersService usersService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;


    @GetMapping("/search/tags")
    public BaseResponse<List<Users>> searchUsersByTags(@RequestParam(required = false) List<String> taglist){
        if(CollectionUtils.isEmpty(taglist)){
            throw new BusinesException(ErrorCode.PARAMS_ERROR);
        }
        return BaseResponse.success(usersService.searchUsersByTags(taglist));
    }
    @PostMapping("/register")
    public BaseResponse register(@RequestBody UserRegisterRequest request){
        if(request==null){
//            return BaseResponse.error(ErrorCode.NULL_ERROR);
            throw new BusinesException(ErrorCode.NULL_ERROR);

        }
        String userAccount = request.getUserAccount();
        String password = request.getPassword();
        String confirmPassword = request.getConfirmPassword();
        String planetCode = request.getPlanetCode();
        if(StringUtils.isAnyBlank(userAccount,password,confirmPassword,planetCode)){
            return BaseResponse.error(ErrorCode.PARAMS_ERROR);
        }
        long result = usersService.userRegister(userAccount,password,confirmPassword,planetCode);
        return BaseResponse.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<Users> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest req){
        if(userLoginRequest==null){
            return BaseResponse.error(ErrorCode.NULL_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String password = userLoginRequest.getPassword();

        if(StringUtils.isAnyBlank(userAccount,password)){
            return BaseResponse.error(ErrorCode.PARAMS_ERROR);
        }
        Users user = usersService.userLogin(userAccount,password,req);
        return BaseResponse.success(user);
    }
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest req){
        if(req==null){
            return null;
        }


        return BaseResponse.success(usersService.userLogout(req)) ;
    }

    @GetMapping("/search")
    public BaseResponse<List<Users>> searchUsers( String username, HttpServletRequest req){
        //1.鉴权,仅管理员
        if(!usersService.isAdmin(req)){
            return BaseResponse.error(ErrorCode.NO_AUTH);
        }
//        QueryWrapper<Users> queryWrapper=new QueryWrapper<>();
//        if(StringUtils.isNotBlank(username)){
//            queryWrapper.like("username",username);
//        }
//        return usersService.list(queryWrapper);
        return BaseResponse.success(usersService.searchUsers(username));
    }


    @GetMapping("/recommend")
    public BaseResponse<Page<Users>> recommendUsers(@RequestParam long pagenum, @RequestParam long pagesize,HttpServletRequest req){
        Users currentUser = usersService.getCurrentUser(req);
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        //缓存中有没有，如果有，直接读缓存
        String redisKey = String.format("leeyou:user:recommend:%s",currentUser.getId());
        Page<Users> userPage  = (Page<Users> )valueOperations.get(redisKey);
        if(userPage != null){
            return BaseResponse.success(userPage);
        }
        //无缓存，查数据库
        QueryWrapper<Users> queryWrapper=new QueryWrapper<>();
        userPage= usersService.page(new Page(pagenum,pagesize),queryWrapper);

        //写缓存
        try {
            valueOperations.set(redisKey,userPage,30000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("redis set key error",e);
        }
        return BaseResponse.success(userPage);
    }


    @PostMapping ("/delete")
    public BaseResponse<Boolean> deleteById(@RequestBody long id, HttpServletRequest req){

        QueryWrapper<Users> queryWrapper=new QueryWrapper<>();
        if(!usersService.isAdmin(req)){
            return BaseResponse.error(ErrorCode.NO_AUTH);
        }
        if(id<=0){
            return  BaseResponse.error(ErrorCode.PARAMS_ERROR);
        }
        return BaseResponse.success(usersService.removeById(id));


    }


    @GetMapping("/current")
    public BaseResponse<Users> getCurrentUser(HttpServletRequest req){
        Object userobject = req.getSession().getAttribute(USER_LOGIN_STATE);
        Users user = (Users)userobject;
        if(user==null){
            throw new BusinesException(ErrorCode.NOT_LOGIN);
        }
        Long id = user.getId();
        Users newuser = usersService.getById(id);
        Users safeUser = usersService.getsaferUser(newuser);
        return BaseResponse.success(safeUser);
    }

    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody Users user, HttpServletRequest req){
        if(user==null){
            return BaseResponse.error(ErrorCode.PARAMS_ERROR);
        }
        Users currentUser = usersService.getCurrentUser(req);
        return BaseResponse.success(usersService.updateUser(user,currentUser));
    }

    /**
     * 获取最匹配的用户
     * @param num
     * @param req
     * @return
     */
    @GetMapping("/match")
    public BaseResponse<List<Users>> matchUsers(long num, HttpServletRequest req){
        if(num <= 0 || num > 20){
            throw new BusinesException(ErrorCode.PARAMS_ERROR);
        }
        return BaseResponse.success(usersService.matchUsers(num,req));
    }
}
