package com.leeyoubackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leeyoubackend.mapper.UsersMapper;
import com.leeyoubackend.pojo.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest
public class insertUsersTest {

        @Autowired
        private UsersService usersService;
        @Autowired
        private UsersMapper usersMapper;

        private ExecutorService executorService = new ThreadPoolExecutor(60,1000,10000, TimeUnit.MINUTES,new ArrayBlockingQueue<>(10000));
        /**
         * 批量插入用户
         */
//    @Scheduled(initialDelay = 5000,fixedRate = Long.MAX_VALUE)
//        @Test
//        public void doInsertUsers(){
//            StopWatch stopWatch = new StopWatch();
//            System.out.println("开始执行===================");
//            stopWatch.start();
//            final int NUM = 100000;
//            List<Users> users = new ArrayList<>();
//            for (int i = 0; i < NUM; i++) {
//                Users user = new Users();
//
//                user.setUsername("假用户"+i);
//                user.setUserAccount("1111"+i);
//                user.setPassword("222222111");
//                user.setAvatarUrl("");
//                user.setGender(0);
//                user.setPhone("2222222221414");
//                user.setEmail("1441444");
//                user.setStatus(0);
//
//
//                user.setRole(0);
//                user.setPlanetCode("1111111111");
//                user.setTags("[]");
//                users.add(user);
//            }
//            //11 s 10万条
//            usersService.saveBatch(users,10000);
//            stopWatch.stop();
//            System.out.println(stopWatch.getTotalTimeMillis());
//        }

    /**
     * 并发批量生成用户
      */
//    @Test
//    public void doConcurrencyInsertUsers(){
//        StopWatch stopWatch = new StopWatch();
//        System.out.println("开始执行===================");
//        stopWatch.start();
//        final int NUM = 100000;
//        int j = 0;
//        int batchsize = 20000;
//        List<CompletableFuture<Void>> futureList = new ArrayList<>();
////        List<Users> users = new ArrayList<>();
//        //分十组
//        for(int i = 0; i < 10; i++){
//            List<Users> userlist = new ArrayList<>();
//            while(true){
//                j++;
//                Users user = new Users();
//                user.setUsername("假用户"+j);
//                user.setUserAccount("1111"+j);
//                user.setPassword("222222111");
//                user.setAvatarUrl("");
//                user.setGender(0);
//                user.setPhone("2222222221414");
//                user.setEmail("1441444");
//                user.setStatus(0);
//                user.setRole(0);
//                user.setPlanetCode("1111111111");
//                user.setTags("[]");
//                userlist.add(user);
//                if(j%10000==0){
//                    break;
//                }
//            }
//            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//                System.out.println("线程名称：" + Thread.currentThread().getName());
//                usersService.saveBatch(userlist, batchsize);
//            }, executorService);
//            futureList.add(future);
//        }
//
//        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
//        stopWatch.stop();
//        System.out.println(stopWatch.getTotalTimeMillis());
//    }
//    @Test
//    public void dodeleteUsers(){
//        QueryWrapper queryWrapper=new QueryWrapper();
//        queryWrapper.gt("id",16763);
//        usersMapper.delete(queryWrapper);
//    }
//
////        public static void main(String[] args) {
////            com.leeyoubackend.once.InsertUsers insertUsers = new com.leeyoubackend.once.InsertUsers();
////            insertUsers.doInsertUsers();
////        }
    }
