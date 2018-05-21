package com.example.sunjie.knowledgepointshareproject.builder.sample1;

import com.example.sunjie.knowledgepointshareproject.builder.sample1.drink.Coke;
import com.example.sunjie.knowledgepointshareproject.builder.sample1.drink.Pepsi;
import com.example.sunjie.knowledgepointshareproject.builder.sample1.eat.ChickenBurger;
import com.example.sunjie.knowledgepointshareproject.builder.sample1.eat.VegBurger;

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
