package com.example.knowledgepointsharelib.algorithm.dynamicprogramming;

/**
 * Created by sunjie on 2018/10/2.
 */

public class LongestCommonSubsequence {

    public static void main(String[] args) {
        LongestCommonSubsequence lcs = new LongestCommonSubsequence();
        int findLCS = lcs.findLCS("android", "random");
        System.out.println("最长子序列长度：" + findLCS);
    }

    public int findLCS(String A, String B) {
        int n = A.length();
        int m = B.length();
        char[] a = A.toCharArray();
        char[] b = B.toCharArray();
        int[][] dp = new int[n][m];
//        计算出第一列数值
        for (int i = 0; i < n; i++) {
            if (a[i] == b[0]) {
                dp[i][0] = 1;
                for (int j = i + 1; j < n; j++) {
                    dp[j][0] = 1;
                }
                break;
            }
        }
//        计算出第一行数值
        for (int i = 0; i < m; i++) {
            if (a[0] == b[i]) {
                dp[0][i] = 1;
                for (int j = i + 1; j < m; j++) {
                    dp[0][j] = 1;
                }
                break;
            }
        }
//        矩阵中剩余的数值，通过a[i]是否等于b[j]结合前面的数值计算得出
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                if (a[i] == b[j]) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(dp[i][j] + " ");
            }
            System.out.println();
        }
        return dp[n - 1][m - 1];
    }


}
