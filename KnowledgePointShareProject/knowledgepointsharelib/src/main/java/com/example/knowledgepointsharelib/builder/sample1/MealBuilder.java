package com.example.knowledgepointsharelib.builder.sample1;

import com.example.knowledgepointsharelib.builder.sample1.bugger.ChickenBurger;
import com.example.knowledgepointsharelib.builder.sample1.bugger.VegBurger;
import com.example.knowledgepointsharelib.builder.sample1.dirnk.Coke;
import com.example.knowledgepointsharelib.builder.sample1.dirnk.Pepsi;

/**
 * Created by sunjie on 2018/5/5.
 */

public class MealBuilder {

    public Meal prepareVegMeal() {
        Meal meal = new Meal();
        meal.addItem(new VegBurger());
        meal.addItem(new Coke());
        return meal;
    }

    public Meal prepareNonVegMeal() {
        Meal meal = new Meal();
        meal.addItem(new ChickenBurger());
        meal.addItem(new Pepsi());
        return meal;
    }
}
