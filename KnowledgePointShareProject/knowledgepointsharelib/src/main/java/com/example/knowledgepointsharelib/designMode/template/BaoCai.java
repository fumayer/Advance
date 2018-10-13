package com.example.knowledgepointsharelib.designMode.template;

/**
 * Created by sunjie on 2018/10/13.
 */

public class BaoCai extends Dishes {
    @Override
    public void pourVegetable() {
        System.out.println("下锅的蔬菜是包菜");
    }

    @Override
    public void pourSauce() {
        System.out.println("下锅的酱料是辣椒");
    }
}
