package com.example.sj.lib.testcope.sample;

public class TestCopy {



    public static void main(String[] args) {
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
