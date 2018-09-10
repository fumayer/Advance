package com.example.knowledgepointsharelib.designMode.adapter.sample2.interfaceadapter;

import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

/**
 * Created by sunjie on 2018/6/1.
 */

public class Test {
    private void test(){
        ScaleAnimation animation = new ScaleAnimation(null, null);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
