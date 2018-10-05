package com.example.knowledgepointsharelib.algorithm.other;

/**
 * Created by sunjie on 2018/10/5.
 * 大数相乘
 */

public class BigNumberMultiply {

    public static void main(String[] args) {
        String s1 = "123";
        String s2 = "456";
        String result = multiply(s1, s2);
        System.out.println(s1 + "*" + s2 + " = " + result);

    }

    public static String multiply(String s1, String s2) {
//        符号问题，如果两字符串异号，最后的结果为'-'
        char signA = s1.charAt(0);
        char signB = s2.charAt(0);
        char sign = '+';
        if (signA == '-' && signB != '-') {
            s1 = s1.substring(1);
            sign = signA;
        }
        if (signB == '-' && signA != '-') {
            s2 = s2.substring(1);
            sign = signB;
        }
        if (signB == '-' && signA == '-') {
            s2 = s2.substring(1);
            s1 = s1.substring(1);
        }

//        将字符串翻转，转换为char数组，后面进行逐位相乘
        char[] a = new StringBuilder(s1).reverse().toString().toCharArray();
        char[] b = new StringBuilder(s2).reverse().toString().toCharArray();
        int lenA = a.length;
        int lenB = b.length;

//        结果数组，3位数*3位数，结果最小5位数最大6位数
        int[] result = new int[lenA + lenB];

//        逐位相乘，将结果放到result[]的对应位置，会有不同的数影响同一位，所以使用+=符号
        for (int i = 0; i < lenA; i++) {
            for (int j = 0; j < lenB; j++) {
                result[i + j] += (a[i] - '0') * (b[j] - '0');
            }
        }

//        大于10，进位
        for (int i = 0; i < result.length; i++) {
            if (result[i] >= 10) {
                result[i + 1] += result[i] / 10;
                result[i] %= 10;
            }
        }

//        如果第一位为0，不输出，不存在第二位也是0的情况，因为数组长度规定了
        StringBuilder sb = new StringBuilder();
        if (result[result.length - 1] != 0) {
            sb.append(result[result.length - 1]);
        }
//        将int数组中的元素，从后往前拼接到StringBuilder中
        for (int i = result.length - 2; i >= 0; i--) {
            sb.append(result[i]);
        }
        if (sign == '-') { //如果是异号，在结果中插入'-'
            sb.insert(0, sign);
        }
        return sb.toString();
    }


}