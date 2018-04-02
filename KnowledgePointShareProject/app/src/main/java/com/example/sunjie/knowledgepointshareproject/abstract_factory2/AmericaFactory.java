package com.example.sunjie.knowledgepointshareproject.abstract_factory2;

/**
 * Created by sunjie on 2018/4/2.
 */

public class AmericaFactory implements ContinentFactory {
    @Override
    public Herbivore CreateHerbivore() {
        return new Bison();
    }

    @Override
    public Carnivore CreateCarnivore() {
        return new Wolf();
    }
}
