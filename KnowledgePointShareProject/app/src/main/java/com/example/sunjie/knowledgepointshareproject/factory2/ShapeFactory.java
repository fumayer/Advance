package com.example.sunjie.knowledgepointshareproject.factory2;

/**
 * Created by sunjie on 2018/3/26.
 */

public class ShapeFactory {

    enum ShapeType {
        RING, CIRCLE, POINT
    }

    public static IShape getShape(ShapeType shapeType) {
        IShape iShape=null;
        switch (shapeType) {
            case RING:
                iShape=new RingShape();
                break;
            case CIRCLE:
                iShape=new CircleShape();
                break;
            case POINT:
                iShape=new PointShape();
                break;
            default:
                break;
        }
        return iShape;
    }
}
