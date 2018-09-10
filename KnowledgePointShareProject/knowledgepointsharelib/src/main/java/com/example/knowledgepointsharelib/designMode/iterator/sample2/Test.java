package com.example.knowledgepointsharelib.designMode.iterator.sample2;

/**
 * Created by sunjie on 2018/5/13.
 */

public class Test {

    public static void main(String[] args) {
        Aggregation<String> aggregation = new ConcreteAggregation<String>();
        aggregation.add("张三");
        aggregation.add("李四");
        aggregation.add("王五");
        aggregation.add("赵六");
        Iterator<String> iterator = aggregation.iterator();
        int testCount = 0;
        while (iterator.hasNext()) {
            testCount++;
            System.out.println(iterator.netx());
            if (testCount == 10) {
                break;
            }
        }
    }
}
