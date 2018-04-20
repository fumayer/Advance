package com.example.sunjie.knowledgepointshareproject.filter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.sunjie.knowledgepointshareproject.LogUtil;
import com.example.sunjie.knowledgepointshareproject.R;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {

    private ArrayList<Person> persons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        persons = new ArrayList<>();
        persons.add(new Person("张三", Person.MALE, 10, false, 160));
        persons.add(new Person("李四", Person.MALE, 11, false, 150));
        persons.add(new Person("王五", Person.MALE, 12, true, 170));
        persons.add(new Person("孙六", Person.MALE, 13, true, 180));
        persons.add(new Person("赵七", Person.MALE, 14, false, 165));

        persons.add(new Person("静姐", Person.FEMALE, 14, true, 156));
        persons.add(new Person("驴子", Person.FEMALE, 20, false, 165));
        persons.add(new Person("格格", Person.FEMALE, 30, false, 178));
        persons.add(new Person("赵敏", Person.FEMALE, 32, true, 175));
        persons.add(new Person("张敏", Person.FEMALE, 34, true, 186));
    }

    private Criteria maleCriteria, singleCriteria, singleDogCriteria;

    public void go1(View view) {
        maleCriteria = new MaleCriteria();
        ArrayList<Person> people = maleCriteria.meetCriteria(persons);
        printPersons(people);
    }

    public void go2(View view) {
        singleCriteria = new SingleCriteria();
        ArrayList<Person> people = singleCriteria.meetCriteria(persons);
        printPersons(people);
    }

    public void go3(View view) {
        singleDogCriteria = new SingleDogCriteria(singleCriteria, maleCriteria);
        ArrayList<Person> people = singleDogCriteria.meetCriteria(persons);
        printPersons(people);
    }

    public void go4(View view) {

        printPersons(persons);
    }

    private void printPersons(ArrayList<Person> persons) {
        for (Person person : persons) {
            LogUtil.e("FilterActivity", " " + person);
        }
    }

}
