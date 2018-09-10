package com.example.knowledgepointsharelib.designMode.decorator.sample2;

/**
 * Created by sunjie on 2018/5/21.
 */

//ConreteDecorator 技能：W
public class Skill_W extends Skills{

    private String skillName;

    public Skill_W(Hero hero,String skillName) {
        super(hero);
        this.skillName = skillName;
    }

    @Override
    public void learnSkills() {
        super.learnSkills();
        System.out.println("学习了技能W:" + skillName);
    }
}
