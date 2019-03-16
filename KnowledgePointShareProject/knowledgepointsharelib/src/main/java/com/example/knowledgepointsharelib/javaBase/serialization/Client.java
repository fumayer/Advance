package com.example.knowledgepointsharelib.javaBase.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by sunjie on 2019/3/16.
 */

public class Client {
    private static File file = new File("/Users/sunjie/develop/android/as/work/own/myGit/KnowledgePointShareProject/knowledgepointsharelib/src/main/java/com/example/knowledgepointsharelib/javaBase/serialization/person.txt");

    public static void main(String[] args) {
        try {
            seria(file, new Person("孙杰", 273));
            Person person1 = deSeria(file);
            Person person2 = deSeria(file);
            System.out.println(person1.toString());
            System.out.println("\n" + person2.toString());
            System.out.println("equals:" + person1.equals(person2));
            System.out.println("    ==:" + (person1 == person2));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main2(String[] args) {
        try {
            Person.ANCESTORS = "猿猴";// 序列化之前，修改静态值，结果反序列化回来是正确的
            seria(file, new Person("孙杰", 273));
            Person person = deSeria(file);
            System.out.println(person.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 模拟另一个程序反序列化对象，查看静态的值
     */
    public static void main3(String[] args) throws IOException, ClassNotFoundException {
        Person person = deSeria(file);
        System.out.println(person.toString());
        System.out.println();
    }

    /**
     * 序列化
     */
    public static void seria(File file, Person person) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(person);
        fos.close();
        oos.close();
    }

    /**
     * 反序列化
     */
    public static Person deSeria(File file) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Person object = (Person) ois.readObject();
        fis.close();
        ois.close();
        return object;

    }
}
