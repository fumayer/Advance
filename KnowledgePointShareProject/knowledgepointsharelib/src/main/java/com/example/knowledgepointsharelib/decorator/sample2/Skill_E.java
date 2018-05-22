package com.example.knowledgepointsharelib.decorator.sample2;

/**
 * Created by sunjie on 2018/5/21.
 */

//ConreteDecorator 技能：E
public class Skill_E extends Skills {

    private String skillName;

    public Skill_E(Hero hero, String skillName) {
        super(hero);
        this.skillName = skillName;
    }

    @Override
    public void learnSkills() {
        super.learnSkills();
        System.out.println("学习了技能E:" + skillName);
    }
}
