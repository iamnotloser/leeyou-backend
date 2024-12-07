package com.leeyoubackend.utils;

import java.util.List;

/**
 * 算法工具类
 */
public class AlgorithmUtils {
    /**
     * 原理:https://blog.csdn.net/tianjindong0804/article/details/115803158
     * 编辑距离算法
     * @param word1
     * @param word2
     * @return
     */
    public static int minDistance(List<String> tagList1, List<String> tagList2) {
        if (tagList1 == null || tagList2 == null) {
            throw new RuntimeException("参数不能为空");
        }
        int[][] dp = new int[tagList1.size() + 1][tagList2.size() + 1];
        //初始化DP数组
        for (int i = 0; i <= tagList1.size(); i++) {
            dp[i][0] = i;
        }
        for (int i = 0; i <= tagList2.size(); i++) {
            dp[0][i] = i;
        }
        int cost;
        for (int i = 1; i <= tagList1.size(); i++) {
            for (int j = 1; j <= tagList2.size(); j++) {
                if (tagList1.get(i - 1).equals(tagList2.get(j - 1)) ) {
                    cost = 0;
                } else {
                    cost = 1;
                }
                dp[i][j] = min(dp[i - 1][j] + 1, dp[i][j - 1] + 1, dp[i - 1][j - 1] + cost);
            }
        }
        return dp[tagList1.size()][tagList2.size()];
    }
    private static int min(int x, int y, int z) {
        return Math.min(x, Math.min(y, z));
    }

}
