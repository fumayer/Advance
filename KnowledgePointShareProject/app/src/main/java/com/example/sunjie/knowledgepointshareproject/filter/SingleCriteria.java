package com.example.sunjie.knowledgepointshareproject.filter;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by sunjie on 2018/4/18.
 */

public class SingleCriteria implements Criteria {
    @Override
    public ArrayList<Person> meetCriteria(ArrayList<Person> persons) {
        ArrayList<Person> singlePersons = new ArrayList<>(persons);

        Iterator<Person> iterator = singlePersons.iterator();
        while (iterator.hasNext()) {
            Person person = iterator.next();
            if (person.getMaritalStatus()) {
                iterator.remove();
            }
        }

        return singlePersons;
    }
}
