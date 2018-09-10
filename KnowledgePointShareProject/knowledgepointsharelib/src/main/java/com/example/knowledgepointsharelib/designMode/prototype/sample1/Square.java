package com.example.knowledgepointsharelib.designMode.prototype.sample1;

/**
 * Created by sunjie on 2018/5/25.
 */
public class Square extends Shape {

    public Square(){
        type = "Square";
    }

    @Override
    public void draw() {
        System.out.println("Inside Square::draw() method.");
    }
}
