package com.leeyoubackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leeyoubackend.pojo.Users;
import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ValueOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class redissonTest {

    @Autowired
    private RedissonClient redissonClient;
    @Test
     void test(){
        //list
//        List<String> list = new ArrayList<>();
//        list.add("nihao");
//        System.out.println("list:"+list.get(0));
//        list.remove(0);
////        list.remove(0);
//        RList<String> rList = redissonClient.getList("test-list");
////        rList.add("nihao");
//        System.out.println("rlist:"+rList.get(0));
//        rList.remove(0);
        //map
        Map<String,Integer> map = new HashMap<>();
        map.put("nihao",1);
        map.get("nihao");
//        map.remove("nihao");
        System.out.println("map:"+map.get("nihao"));
        RMap<String, Integer> rMap = redissonClient.getMap("test-map");
        rMap.put("nihao",1);
        System.out.println("rmap:"+rMap.get("nihao"));
        //stack
    }

//    @Test
//    void testWatchDog(){
//        RLock lock = redissonClient.getLock("leeyou:precachejob:docache:lock");
//        try {
//            if (lock.tryLock(0,-1, TimeUnit.MILLISECONDS)) {
//                Thread.sleep(300000);
//                System.out.println("lock--------------"+Thread.currentThread().getId());
//                // 预热缓存
//
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
////        finally {
////            if(lock.isHeldByCurrentThread()){
////                System.out.println("unlock--------------"+Thread.currentThread().getId());
////                lock.unlock();
////            }
////        }
//    }
}
