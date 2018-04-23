package com.example.testlibrary;

/**
 * Created by sunjie on 2018/4/22.
 */

public class TestString {
    public static void main(String[] args){
        String n = "I Love Java";
        String m = "I Love Java";

        System.err.println(n==m);

        n=n+" ";
        System.err.println(n==m);
    }
}
