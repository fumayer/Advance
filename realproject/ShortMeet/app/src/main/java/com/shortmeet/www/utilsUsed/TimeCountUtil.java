package com.shortmeet.www.utilsUsed;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.shortmeet.www.R;

/**
 * 验证码倒计时器  工具类  （需要 结合贪婪鬼倒计时的工具类 做整改）
 */

public class TimeCountUtil extends CountDownTimer {
    private Activity mActivity;
    private TextView btn;//按钮

    // 在这个构造方法里需要传入三个参数，一个是Activity，一个是总的时间millisInFuture，一个是countDownInterval，然后就是你在哪个按钮上做这个是，就把这个按钮传过来就可以了
    public TimeCountUtil(Activity mActivity, long millisInFuture, long countDownInterval, TextView btn) {
        super(millisInFuture, countDownInterval);
        this.mActivity = mActivity;
        this.btn = btn;
    }


    @SuppressLint("NewApi")
    @Override
    public void onTick(long millisUntilFinished) {
        btn.setClickable(false);//设置不能点击
        String text = "("+millisUntilFinished / 1000 + "S)后重试";
        btn.setText(text);//设置倒计时时间

        //设置按钮为灰色，这时是不能点击的
        btn.setBackgroundResource(R.drawable.shape_regist_keydownbtgetyzm);
        btn.setTextColor(ContextCompat.getColor(mActivity.getBaseContext(),R.color.black_4));
        Spannable span = new SpannableString(btn.getText().toString());//获取按钮的文字
        span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.black_4)), 1, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//讲倒计时时间显示为红色
        btn.setText(span);
    }


    @SuppressLint("NewApi")
    @Override
    public void onFinish() {
        btn.setText("获取验证码");
        btn.setClickable(true);//重新获得点击
        //用时释放并设置颜色
        btn.setBackground(ContextCompat.getDrawable(mActivity.getBaseContext(), R.drawable.shape_regist_activity_btgetyzm));//还原背景色
        btn.setTextColor(ContextCompat.getColor(mActivity.getBaseContext(), R.color.getverify));

    }


}
