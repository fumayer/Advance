package com.example.sunjie.knowledgepointshareproject.factory;

/**
 * Created by sunjie on 2018/3/25.
 */

public class ShapeFactory {

    enum ShapeType {
        RING, OVAL, LINE, RECTANGLE;
    }

    public static IShape getShape(ShapeType shapeType) {
        switch (shapeType) {
            case LINE:
                return new LineShape();
            case OVAL:
                return new OvalShape();
            case RING:
                return new RingShape();
            case RECTANGLE:
                return new RectangleShape();
            default:
                break;
        }
        return null;
    }
}
