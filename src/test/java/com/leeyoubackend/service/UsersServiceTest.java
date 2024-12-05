package com.leeyoubackend.service;

import com.leeyoubackend.pojo.Users;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
@SpringBootTest
class UsersServiceTest {
    @Autowired
    private UsersService usersService;
    @Test
    void searchUsersByTags() {
        List<String> tagnamelist = Arrays.asList("java","python");
        List<Users> users = usersService.searchUsersByTags(tagnamelist);
        Assert.assertNotNull(users);

    }
}