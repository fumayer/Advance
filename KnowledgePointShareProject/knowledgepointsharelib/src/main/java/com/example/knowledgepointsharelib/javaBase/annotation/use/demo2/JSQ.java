package com.example.knowledgepointsharelib.javaBase.annotation.use.demo2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by sunjie on 2019/3/21.
 */

public class JSQ {
    public static void jsq(Client client) {
        Class<? extends Client> clientClass = client.getClass();
        Method[] methods = clientClass.getDeclaredMethods();
        for (Method method : methods) {
//            查看方法是否有注解，如果有，调用该方法，看是否有会出错，
            if (method.isAnnotationPresent(Check.class)) {
                method.setAccessible(true);
                try {
                    method.invoke(client, null);
                } catch (Exception e) {
                    System.out.println(method.getName() + "():方法有bug");
//                    e.printStackTrace();
                }
            }
        }

    }
}
