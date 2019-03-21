package com.example.knowledgepointsharelib.javaBase.annotation.use.demo1;

import java.lang.reflect.Field;

/**
 * 注解解释器
 */

public class JSQ {
    public static void jsq(Person person) throws IllegalAccessException {
//        反射，使用实例获取类对象
         Class<? extends Person> personClass = person.getClass();
//         类对象获取属性集合
        Field[] fields = personClass.getDeclaredFields();
//        遍历属性集合
        for (Field field : fields) {
//            判断该属性是否使用了 某一个注解（AssignString注解）
            if (field.isAnnotationPresent(AssignString.class)) {
//                获取该属性的注解，然后获取注解的值value，最后将值赋值给属性
                AssignString assignString = field.getAnnotation(AssignString.class);
                String nameValue=assignString.value();
                field.setAccessible(true);
                field.set(person,nameValue);
            }
            if (field.isAnnotationPresent(AssignInt.class)) {
                AssignInt assignInt = field.getAnnotation(AssignInt.class);
                field.setAccessible(true);
                field.set(person,assignInt.value());
            }

        }
    }
}
