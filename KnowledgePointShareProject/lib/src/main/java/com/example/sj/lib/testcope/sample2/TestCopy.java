package com.example.sj.lib.testcope.sample2;

public class TestCopy {

//    稍微复杂的深拷贝(deep copy)

    public static void main(String[] args) {
        Address address = new Address("山西");
        Student originalStudent = new Student(0, address);
        System.out.println("原始数据：  " + originalStudent);
        Student copyStudent =  originalStudent.clone();
        System.out.println("拷贝数据：  " + copyStudent);
        copyStudent.setNumber(1);
        copyStudent.getAddress().setAdd("北京");
        System.out.println("改变的拷贝数据： " + copyStudent + "        原始数据： " + originalStudent);
        System.out.println("==比较：  " + (originalStudent == copyStudent));
        System.out.println("equals比较：  " + originalStudent.equals(copyStudent));
    }


}
