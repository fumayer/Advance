package com.example.knowledgepointsharelib.designMode.decorator.sample1;

/**
 * Created by sunjie on 2018/5/21.
 */

public abstract class ShapeDecorator implements Shape {
    protected Shape decoratedShape;

    public ShapeDecorator(Shape decoratedShape){
        this.decoratedShape = decoratedShape;
    }

    @Override
    public void draw(){
        decoratedShape.draw();
    }
}