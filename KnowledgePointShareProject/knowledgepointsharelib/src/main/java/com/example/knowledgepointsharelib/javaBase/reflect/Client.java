package com.example.knowledgepointsharelib.javaBase.reflect;

import android.annotation.SuppressLint;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by sunjie on 2019/3/15.
 */

public class Client {
    public static void main1(String[] args) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {

        Class clazz = Class.forName("com.example.knowledgepointsharelib.javaBase.reflect.Reflect");
//        反射获取该属性
        Field fl = clazz.getDeclaredField("SEX");
        if (!fl.isAccessible()) {
            fl.setAccessible(true);
            System.out.println("设置为可以访问");
        }
//        获取属性的值
        String gf = (String) fl.get(clazz);
        System.out.println(gf);
//        修改属性的值
        fl.set(null, "WOMEN");
        System.out.println(Reflect.SEX);
    }

    @SuppressLint("WrongConstant")
    public static void main(String[] args) throws ClassNotFoundException {

        Class clazz = Class.forName("com.example.knowledgepointsharelib.javaBase.reflect.Reflect");

        System.out.println("------获取类名-----------");
        System.out.println(clazz.toString());// 获取类的完整名
        System.out.println(clazz.getSimpleName());// 获取类名

        System.out.println("------获取类中的构造方法-----------");
        Constructor[] cons = clazz.getDeclaredConstructors(); // 获得所有构造方法
        for (int i = 0; i < cons.length; i++) {
            System.out.println("所有构造方法之" + cons[i].getName());
            System.out.println("修饰符类型：" + Modifier.toString(cons[i].getModifiers())); // 构造方法修饰符
        }
        Constructor[] cons1 = clazz.getConstructors(); // 获得所有公共构造方法
        for (int i = 0; i < cons1.length; i++) {
//            System.out.println("公共构造方法之" + cons1[i].getName());
//            System.out.println("修饰符类型：" + cons1[i].getModifiers());
        }

        System.out.println("-----获取类中定义的方法------------");
        Method[] mths = clazz.getDeclaredMethods();
        for (int i = 0; i < mths.length; i++) {
            System.out.println("所有方法之:" + mths[i].toGenericString());
//            System.out.println("修饰符类型：" + Modifier.toString(mths[i].getModifiers()));
        }
        Method[] mths1 = clazz.getMethods();
        for (int i = 0; i < mths1.length; i++) {
//            System.out.println("公有方法之" + mths1[i].toGenericString());
//            System.out.println("修饰符类型：" + Modifier.toString(mths1[i].getModifiers()));
        }

        System.out.println("-------获取所有的属性------------");
        Field[] fls1 = clazz.getDeclaredFields();
        for (int i = 0; i < fls1.length; i++) {
            System.out.println("所有属性之" + fls1[i].getName());
//            System.out.println("修饰符类型：" + fls1[i].getModifiers());
        }
        Field[] fls = clazz.getFields();
        for (int i = 0; i < fls.length; i++) {
//            System.out.println("公有属性之" + fls[i].getName());
//            System.out.println("修饰符类型：" + fls[i].getModifiers());
        }
    }
}
