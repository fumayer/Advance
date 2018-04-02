package com.example.sunjie.knowledgepointshareproject.abstract_factory2;

/**
 * Created by sunjie on 2018/4/2.
 */

public class AfricaFactory implements ContinentFactory {
    @Override
    public Herbivore CreateHerbivore() {
        return new Wildebeest();
    }

    @Override
    public Carnivore CreateCarnivore() {
        return new Lion();
    }
}
