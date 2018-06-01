package com.example.sj.lib.testcope.sample1;

import java.util.Date;

public class TestCopy {

//    稍微复杂的深拷贝(deep copy)


/*
    一般步骤是（浅拷贝）：
            1. 被拷贝的类需要实现Clonenable接口
              （不实现的话在调用clone方法会抛出CloneNotSupportedException异常)
               该接口为标记接口(不含任何方法)
            2. 覆盖clone()方法，访问修饰符设为public。
               方法中调用super.clone()方法得到需要的拷贝对象，
              （native为本地方法)
    下面的拷贝被称为浅拷贝(Shallow Copy)  浅拷贝对于引用类型的变量只是对引用进行拷贝
*/
    public static void main(String[] args) {
        Address address = new Address("山西");
        Student originalStudent = new Student(0, address);
        System.out.println("原始数据：  " + originalStudent);
        Student copyStudent = (Student) originalStudent.clone();
        System.out.println("拷贝数据：  " + copyStudent);
        copyStudent.setNumber(1);
        // 浅拷贝只是拷贝了address变量的引用，并没有真正的开辟另一块空间，将值拷贝后再将引用返回给新对象
        copyStudent.getAddress().setAdd("北京");
        System.out.println("改变的拷贝数据： " + copyStudent + "        原始数据： " + originalStudent);
        System.out.println("==比较：  " + (originalStudent == copyStudent));
        System.out.println("equals比较：  " + originalStudent.equals(copyStudent));
    }



    public static void main7(String[] args) {
        Student originalStudent = new Student(0);
        System.out.println("原始数据：  " + originalStudent);
        Student copyStudent = (Student) originalStudent.clone();
        System.out.println("拷贝数据：  " + copyStudent);
        System.out.println("改变的拷贝数据： " + copyStudent + "        原始数据： " + originalStudent);
        System.out.println("==比较：  " + (originalStudent == copyStudent));
        System.out.println("equals比较：  " + originalStudent.equals(copyStudent));
    }

    public static void main6(String[] args) {
        Student originalStudent = new Student(0);
        System.out.println("原始数据：  " + originalStudent);
        Student copyStudent = (Student) originalStudent.clone();
        System.out.println("拷贝数据：  " + copyStudent);
        copyStudent.setNumber(1);
        System.out.println("改变的拷贝数据： " + copyStudent + "        原始数据： " + originalStudent);
        System.out.println("==比较：  " + (originalStudent == copyStudent));
        System.out.println("equals比较：  " + originalStudent.equals(copyStudent));
    }

    public static void main5(String[] args) {
        Student originalStudent = new Student(0);
        System.out.println("原始数据：  " + originalStudent);
        Student copyStudent = originalStudent;  // 该语句的作用是将originalStudent的引用赋值给copyStudent
        System.out.println("拷贝数据：  " + copyStudent);
        copyStudent.setNumber(1);
        System.out.println("改变的拷贝数据： " + copyStudent + "        原始数据： " + originalStudent);
        System.out.println("==比较：  " + (originalStudent == copyStudent));
        System.out.println("equals比较：  " + originalStudent.equals(copyStudent));
    }

    public static void main4(String[] args) {
        String originalString = new String("a");
        System.out.println("原始数据：  " + originalString);
        String copyString = originalString;
        System.out.println("拷贝数据：  " + copyString);
        copyString = "b";
        System.out.println("改变的拷贝数据： " + copyString + "        原始数据： " + originalString);
    }

    public static void main3(String[] args) {
        String originalString = "a";
        System.out.println("原始数据：  " + originalString);
        String copyString = originalString;
        System.out.println("拷贝数据：  " + copyString);
        copyString = "b";
        System.out.println("改变的拷贝数据： " + copyString + "        原始数据： " + originalString);
    }

    public static void main2(String[] args) {
        char originalChar = 'a';
        System.out.println("原始数据：  " + originalChar);
        char copyChar = originalChar;
        System.out.println("拷贝数据：  " + copyChar);
        copyChar = 'b';
        System.out.println("改变的拷贝数据： " + copyChar + "        原始数据： " + originalChar);
    }

    public static void main1(String[] args) {
        int originalInt = 10;
        System.out.println("原始数据：  " + originalInt);
        int copyInt = originalInt;
        System.out.println("拷贝数据：  " + copyInt);
        copyInt = ++copyInt;
        System.out.println("改变的拷贝数据： " + copyInt + "        原始数据： " + originalInt);
    }


}
