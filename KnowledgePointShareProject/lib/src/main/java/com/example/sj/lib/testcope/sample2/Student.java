package com.example.sj.lib.testcope.sample2;

/**
 * Created by sunjie on 2018/5/31.
 */

public class Student implements Cloneable {
    private int number;
    private Address address;

    public Student(int number, Address address) {
        this.number = number;
        this.address = address;
    }

    public Student(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Student{" +
                "number=" + number +
                ", address=" + address +
                '}';
    }

    @Override
    protected Student clone() {
        Student student = null;
        try {
            student = (Student) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
//        这样直接操作是不对的，应该使用clone对象
//        address = (Address) address.clone();
        if (student != null) {
            student.address = (Address) address.clone();
        }
        return student;
    }


}
