package com.example.sunjie.knowledgepointshareproject.filter;

import java.util.ArrayList;

/**
 * Created by sunjie on 2018/4/18.
 */

public class MaleCriteria implements Criteria {
    @Override
    public ArrayList<Person> meetCriteria(ArrayList<Person> persons) {
        ArrayList<Person> malePersons = new ArrayList<>();
        for (Person person : persons) {
            if (person.getSex().equals(Person.MALE)) {
                malePersons.add(person);
            }
        }
        return malePersons;
    }
}
