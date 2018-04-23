package com.example.sunjie.knowledgepointshareproject.filter;

import java.util.ArrayList;

/**
 * Created by sunjie on 2018/4/18.
 */

public interface Criteria {
    ArrayList<Person> meetCriteria(ArrayList<Person> persons);
}
