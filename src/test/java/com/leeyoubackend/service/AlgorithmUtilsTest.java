package com.leeyoubackend.service;

import com.leeyoubackend.utils.AlgorithmUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 算法工具类
 */
public class AlgorithmUtilsTest {

    @Test
     void test() {
        String word1 = "horse";
        String word2 = "ros";
        String word3 = "我喜欢你";
        String word4 = "我爱你";
        String word5 = "我爱你中国";
//        System.out.println(AlgorithmUtils.minDistance(word3, word4));
//        System.out.println(AlgorithmUtils.minDistance(word5, word3));
////        int distance1 = AlgorithmUtils.minDistance(word1, word2);
//        System.out.println(distance1);
    }
    @Test
    void test2() {
        List<String> a = new ArrayList<>();
        String[] b = {"java", "love", "leetcode", "学生"};
        a = Arrays.asList(b);
        List<String> b1 = Arrays.asList("C++", "love", "刷题", "student");
        List<String> c = Arrays.asList("java", "love", "社畜", "牛马");
        System.out.println(AlgorithmUtils.minDistance(a, b1));
        System.out.println(AlgorithmUtils.minDistance(a, c));


    }

}
