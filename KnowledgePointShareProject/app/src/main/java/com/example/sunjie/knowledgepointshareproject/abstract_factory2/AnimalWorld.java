package com.example.sunjie.knowledgepointshareproject.abstract_factory2;

/**
 * Created by sunjie on 2018/4/2.
 */

public class AnimalWorld {
    private Herbivore herbivore;
    private Carnivore carnivore;

    public AnimalWorld(ContinentFactory factory) {
        carnivore = factory.CreateCarnivore();
        herbivore = factory.CreateHerbivore();
    }

    // Methods
    public void RunFoodChain() {
        carnivore.Eat(herbivore);
    }

}
