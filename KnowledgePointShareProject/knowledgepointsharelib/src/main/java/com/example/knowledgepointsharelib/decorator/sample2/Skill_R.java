package com.example.knowledgepointsharelib.decorator.sample2;

/**
 * Created by sunjie on 2018/5/21.
 */

//ConreteDecorator 技能：R
public class Skill_R extends Skills {

    private String skillName;

    public Skill_R(Hero hero, String skillName) {
        super(hero);
        this.skillName = skillName;
    }

    @Override
    public void learnSkills() {
        super.learnSkills();
        System.out.println("学习了技能R:" + skillName);
    }
}