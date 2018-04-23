package com.example.sunjie.knowledgepointshareproject.filter;

import java.util.ArrayList;

/**
 * Created by sunjie on 2018/4/18.
 */

public class SingleDogCriteria implements Criteria {
    private Criteria singleCriteria;
    private Criteria maleCriteria;

    public SingleDogCriteria(Criteria singleCriteria, Criteria maleCriteria) {
        this.singleCriteria = singleCriteria;
        this.maleCriteria = maleCriteria;
    }
    @Override
    public ArrayList<Person> meetCriteria(ArrayList<Person> persons) {
        return singleCriteria.meetCriteria(maleCriteria.meetCriteria(persons));
    }
}
