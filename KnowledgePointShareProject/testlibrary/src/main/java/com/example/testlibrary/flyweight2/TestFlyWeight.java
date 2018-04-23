package com.example.testlibrary.flyweight2;

/**
 * Created by sunjie on 2018/4/22.
 */

public class TestFlyWeight {
    private static final String colors[] =
            {"Red", "Green", "Blue", "White", "Black"};

    public static void main1(String[] args) {

        for (int i = 0; i < 20; ++i) {
            Circle circle = (Circle) ShapeFactory.getCircle(getRandomColor());
            circle.setX(getRandomX());
            circle.setY(getRandomY());
            circle.setRadius(100);
            circle.draw();
        }
    }

    private static String getRandomColor() {
        return colors[(int) (Math.random() * colors.length)];
    }

    private static int getRandomX() {
        return (int) (Math.random() * 100);
    }

    private static int getRandomY() {
        return (int) (Math.random() * 100);
    }

    public static void main(String[] args) {

        Circle circle_red = (Circle) ShapeFactory.getCircle("Red");
        circle_red.setX(1);
        circle_red.setY(1);
        circle_red.setRadius(1);
        circle_red.draw();
        System.err.println("----------------");

        Circle circle_green = (Circle) ShapeFactory.getCircle("Red");
        circle_green.setX(2);
        circle_green.setY(2);
        circle_green.setRadius(2);
        circle_green.draw();

        System.err.println("----------------");
        circle_red.draw();

    }

}
