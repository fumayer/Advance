package com.example.knowledgepointsharelib.algorithm.other;

import java.util.Stack;

/**
 * Created by sunjie on 2018/10/16.
 */

public class StackMinValue {

    public static void main(String[] args) {
        StackMinValue stackMinValue = new StackMinValue();
        stackMinValue.test();
    }

    //自定义栈，min函数得到当前最小值
    public void test() {
        MinStack ms = new MinStack();
        ms.push(5);
        System.out.println(ms.min());
        ms.push(6);
        ms.push(2);
        ms.push(1);
        System.out.println(ms.min());
        ms.pop();
        System.out.println(ms.min());
        ms.pop();
        System.out.println(ms.min());
    }

    class MinStack {
        /*存储当前最小的栈，就是剩下的元素中最小的*/
        private Stack<Integer> minStack = new Stack<Integer>();
        /*存放当前数据的栈*/
        private Stack<Integer> stack = new Stack<Integer>();

        public int pop() {
            minStack.pop();
            return stack.pop();
        }

        public void push(int num) {
            stack.push(num);
//            第一个元素直接放
            if (minStack.size() <= 0) {
                minStack.push(num);
                return;
            }
//            下一个元素和上一个元素比较，如果小就放，大就再放一次上一个元素
            Integer min = minStack.lastElement();
            if (num < min) {
                minStack.push(num);
            } else {
                minStack.push(min);
            }
        }

        public int min() {
            if (minStack.size() <= 0) {
                return -1;
            }
            return minStack.lastElement();
        }
    }
}

