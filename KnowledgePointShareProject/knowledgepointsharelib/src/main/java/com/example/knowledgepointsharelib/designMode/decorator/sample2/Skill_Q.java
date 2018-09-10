package com.example.knowledgepointsharelib.designMode.decorator.sample2;

/**
 * Created by sunjie on 2018/5/21.
 */

//ConreteDecorator 技能：Q
public class Skill_Q extends Skills {

    private String skillName;

    public Skill_Q(Hero hero, String skillName) {
        super(hero);
        this.skillName = skillName;
    }

    @Override
    public void learnSkills() {
        super.learnSkills();
        System.out.println("学习了技能Q:" + skillName);
    }
}