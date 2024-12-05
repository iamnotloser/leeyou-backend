package com.leeyoubackend.once;
import java.util.Date;

import com.leeyoubackend.mapper.UsersMapper;
import com.leeyoubackend.pojo.Users;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
public class InsertUsers {
    @Autowired
    private UsersMapper usersMapper;

    /**
     * 批量插入用户
     */
//    @Scheduled(initialDelay = 5000,fixedRate = Long.MAX_VALUE)
//    public void doInsertUsers(){
//        StopWatch stopWatch = new StopWatch();
//        System.out.println("开始执行===================");
//        stopWatch.start();
//        final int NUM = 10000000;
//        for (int i = 0; i < NUM; i++) {
//            Users user = new Users();
//
//            user.setUsername("假用户");
//            user.setUserAccount("1111"+i);
//            user.setPassword("222222111");
//            user.setAvatarUrl("");
//            user.setGender(0);
//            user.setPhone("2222222221414");
//            user.setEmail("1441444");
//            user.setStatus(0);
//
//
//            user.setRole(0);
//            user.setPlanetCode("1111111111");
//            user.setTags("[]");
//            usersMapper.insert(user);
//        }
//        stopWatch.stop();
//        System.out.println(stopWatch.getTotalTimeMillis());
//    }
//
//    public static void main(String[] args) {
//        InsertUsers insertUsers = new InsertUsers();
//        insertUsers.doInsertUsers();
//    }
}