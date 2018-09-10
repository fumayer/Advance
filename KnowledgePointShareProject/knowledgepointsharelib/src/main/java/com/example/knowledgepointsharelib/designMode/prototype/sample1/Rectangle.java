package com.example.knowledgepointsharelib.designMode.prototype.sample1;

/**
 * Created by sunjie on 2018/5/25.
 */
public class Rectangle extends Shape {

    public Rectangle(){
        type = "Rectangle";
    }

    @Override
    public void draw() {
        System.out.println("Inside Rectangle::draw() method.");
    }
}
