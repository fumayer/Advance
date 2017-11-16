package com.shortmeet.www.utilsUsed;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

/**
 * Created by zxf on 2017/11/1.
 */

public class AnimationUtils {
    /**
     *缩放动画
     */
   static  Animation scaleAnimation = null;
    public static void scaleAnimation(View view,boolean reset){
      /*  ViewCompat.animate(view) //实现动画的操作
                .setDuration(200)
                .scaleX(0.9f)
                .scaleY(0.9f)
               // .setInterpolator(new CycleInterpolator())  //差值器，可以选择很多弹性效果
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {

                    }
                    @Override
                    public void onAnimationEnd(final View view) {

                    }

                    @Override
                    public void onAnimationCancel(final View view) {

                    }
                })
                .withLayer()  //渲染作用，不加这句话，动画不会动作
                .start();*/


 

        if (scaleAnimation==null){
            scaleAnimation = new ScaleAnimation(1.0f, 0.8f, 1.0f, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        if (!reset){
            scaleAnimation.setDuration(500);//设置动画持续时间为500毫秒
            scaleAnimation.setFillAfter(true);//如果fillAfter的值为true,则动画执行后，控件将停留在执行结束的状态
            scaleAnimation.setRepeatMode(Animation.REVERSE);
            scaleAnimation.setStartOffset(0);
            view.startAnimation(scaleAnimation);
            return;
        }
           scaleAnimation.setFillAfter(false);

    }

    /**
     * 透明度动画
     * @param view
     */
   static  Animation alphaAnimation = null;
    public static void alphAnimation(View view,boolean stop){
        if(alphaAnimation == null ){
            alphaAnimation = new AlphaAnimation(1.0f, 0.3f);
        }
        if (!stop ){
            alphaAnimation.setDuration(600);//设置动画持续时间为500毫秒
            alphaAnimation.setRepeatCount(Integer.MAX_VALUE);
            alphaAnimation.setFillAfter(false);//设置动画结束后保持当前的位置（即不返回到动画开始前的位置）
            view.startAnimation(alphaAnimation);
            return;
        }
        alphaAnimation.cancel();

    }


    /**
     * 旋转动画
     */
   static  Animation rotateAnimation=null;
    public static void rotateAnimation(View view, int fromRotate,int toRate){
        if (rotateAnimation==null){
            rotateAnimation = new RotateAnimation(fromRotate, toRate, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }else{
            rotateAnimation.cancel();;
            rotateAnimation = new RotateAnimation(fromRotate, toRate, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        rotateAnimation.setDuration(500);
        rotateAnimation.setFillAfter(true);
        view.startAnimation(rotateAnimation);

    }
}
