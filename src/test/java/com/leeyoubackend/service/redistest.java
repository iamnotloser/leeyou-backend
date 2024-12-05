package com.leeyoubackend.service;

import com.leeyoubackend.pojo.Users;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest
public class redistest {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Test
     void test(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //增
        valueOperations.set("name","leeyou");
        valueOperations.set("gender","男");
        valueOperations.set("age","25");
        Users user = new Users();
        user.setUsername("leeyou");
        user.setUserAccount("leeyou");
        user.setPassword("leeyou");
        user.setAvatarUrl("leeyou");
        user.setGender(1);
        valueOperations.set("user",user);
        String name = (String) valueOperations.get("name");
        Assert.assertEquals("leeyou",name);
        System.out.println(valueOperations.get("name"));
        redisTemplate.delete("name");
    }
}
