package com.example.knowledgepointsharelib.prototype.sample2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunjie on 2018/5/31.
 */

/*
*原型必须实现Cloneable接口
*/
public class Worker implements Cloneable {

    private String name; // 姓名
    private Position position; // 职位
    private List<String> subordinate;// 下属

    @Override
    public Worker clone() {
        Worker worker = null;
        try {
            worker = (Worker) super.clone();
            worker.position = position.clone();
            List<String> subordinate = new ArrayList<String>();
            for (String friend : this.getSubordinate()) {
                subordinate.add(friend);
            }
            subordinate.add(name);
            worker.setSubordinate(subordinate);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return worker;
    }

    public Worker(String name, Position position, List<String> subordinate) {
        this.name = name;
        this.position = position;
        this.subordinate = subordinate;
    }

    public List<String> getSubordinate() {
        return subordinate;
    }

    public void setSubordinate(List<String> subordinate) {
        this.subordinate = subordinate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "name='" + name + '\'' +
                ", position=" + position +
                ", subordinate=" + subordinate +
                '}';
    }
}