package com.example.sj.lib.testcope.sample2;

/**
 * Created by sunjie on 2018/5/31.
 */

public class Address implements Cloneable {
    private String add;
    public Address(String add) {
        this.add = add;
    }
    public String getAdd() {
        return add;
    }
    public void setAdd(String add) {
        this.add = add;
    }
    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Address{" +
                "add='" + add + '\'' +
                '}';
    }
}
