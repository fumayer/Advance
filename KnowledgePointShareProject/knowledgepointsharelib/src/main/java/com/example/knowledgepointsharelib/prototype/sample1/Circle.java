package com.example.knowledgepointsharelib.prototype.sample1;

/**
 * Created by sunjie on 2018/5/25.
 */
public class Circle extends Shape {

    public Circle(){
        type = "Circle";
    }

    @Override
    public void draw() {
        System.out.println("Inside Circle::draw() method.");
    }
}
