package com.example.sj.lib.testcope.sample1;

/**
 * Created by sunjie on 2018/5/31.
 */

public class Address {
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
    public String toString() {
        return "Address{" +
                "add='" + add + '\'' +
                '}';
    }
}
