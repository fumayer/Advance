package com.example.knowledgepointsharelib.designMode.prototype.sample2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunjie on 2018/5/31.
 */

public class Test {
    public static void main(String[] args) {

        List<String> subordinate = new ArrayList<>();
        subordinate.add("孙杰");
        subordinate.add("辛峰");
        Position position = new Position("客户端负责人");
        Worker workerA = new Worker("陈惠群", position, subordinate);
        workerA.setSubordinate(subordinate);

        Worker workerB = workerA.clone();
        workerB.setName("任福新");
        workerB.getPosition().setJobTitle("CTO");
        workerB.getSubordinate().add("安然");
        workerB.getSubordinate().add("郭鹏");
        System.out.println("\n" + workerA);
        System.out.println(workerB);
    }
}
