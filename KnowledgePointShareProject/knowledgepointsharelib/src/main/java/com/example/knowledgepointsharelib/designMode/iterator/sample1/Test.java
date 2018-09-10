package com.example.knowledgepointsharelib.designMode.iterator.sample1;

/**
 * Created by sunjie on 2018/5/13.
 */

public class Test {
    public static void main(String[] args) {
        NameRepository namesRepository = new NameRepository();

        for(Iterator iter = namesRepository.getIterator(); iter.hasNext();){
            String name = (String)iter.next();
            System.out.println("Name : " + name);
        }
    }
}
