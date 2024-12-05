package com.leeyoubackend.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leeyoubackend.pojo.Users;
import com.leeyoubackend.service.UsersService;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 预热缓存任务
 */
@EnableAsync
@EnableScheduling
@Component
//@Slf4j
public class PreCacheJob {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private UsersService usersService;
    @Autowired
    private RedissonClient redissonClient;
    //重点用户
    private List<Long> mainuserlist = Arrays.asList(1L);
    @Scheduled(cron = "0 26 15 * * ?")
    public void preCache() {
        RLock lock = redissonClient.getLock("leeyou:precachejob:docache:lock");
        try {
            if (lock.tryLock(0,30000,TimeUnit.MILLISECONDS)) {
                System.out.println("lock--------------"+Thread.currentThread().getId());
                // 预热缓存
                for (Long userid:mainuserlist) {
                    QueryWrapper<Users> queryWrapper=new QueryWrapper<>();
                    Page<Users> userPage= usersService.page(new Page(1,20),queryWrapper);
                    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
                    String redisKey = String.format("leeyou:user:recommend:%s",userid);

                    try {
                        valueOperations.set(redisKey,userPage,30000, TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            if(lock.isHeldByCurrentThread()){
                System.out.println("unlock--------------"+Thread.currentThread().getId());
                lock.unlock();
            }
        }
    }
}
