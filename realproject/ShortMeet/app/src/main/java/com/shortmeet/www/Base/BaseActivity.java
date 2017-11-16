package com.shortmeet.www.Base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shortmeet.www.R;
import com.shortmeet.www.utilsUsed.DialogUtils;
import com.shortmeet.www.utilsUsed.SystemUtils;
import com.shortmeet.www.utilsUsed.ToastUtils;


/**
 * Created by Fenglingyue on 2017/6/26.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public BaseActivity mActivitySelf;
    public FragmentManager mFragmentManager;
    public LayoutInflater mLayoutInflater;
    public View  titleView;  //标题栏
    //标题栏上的控件
    public FrameLayout titleBarLeftframelayout;
    public ImageView imgvLeftbackTitlebar;
    public TextView tvCenterTitleBar;
    public TextView tvRightTitlebar;

    // Fly 注：设置界面跟布局
    public abstract int setRootView();

    // Fly 注：初始化控件
    public abstract void initView();

    // Fly 注： 初始化监听
     public abstract  void  initListener();

    // Fly 注：初始化数据
    public abstract  void initData();

     // Fly 注：设置是否需要标题栏
     public boolean  setUseTitleBar(){
         return false;
     }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUtils.hideBottomUIMenu(this);
        mActivitySelf=this;
        CtrollAllactivitys.addActivity(this);
        mFragmentManager=this.getSupportFragmentManager();
        mLayoutInflater=LayoutInflater.from(this);
        setContentView(setMyContentView());
        initView();
        initListener();
        initData();
    }


    public View setMyContentView(){
      if(setUseTitleBar()){
          LinearLayout linearLayout=new LinearLayout(this);
          linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
          linearLayout.setOrientation(LinearLayout.VERTICAL);
          titleView=mLayoutInflater.inflate(R.layout.layout_titlebar,linearLayout,false);
          titleBarLeftframelayout = (FrameLayout) titleView.findViewById(R.id.title_bar_leftframelayout);
          imgvLeftbackTitlebar = (ImageView)titleView. findViewById(R.id.imgv_leftback_titlebar);
          tvCenterTitleBar = (TextView)titleView. findViewById(R.id.tv_center_title_bar);
          tvRightTitlebar = (TextView) titleView.findViewById(R.id.tv_right_titlebar);
          View rootView=mLayoutInflater.inflate(setRootView(),linearLayout,false);
          linearLayout.addView(titleView);
          linearLayout.addView(rootView);
          return  linearLayout;
      }else{
          return mLayoutInflater.inflate(setRootView(),null);
      }
    }

//    // Fly 注：点击空白处 隐藏软键盘
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if(null!=this.getCurrentFocus()&&getCurrentFocus().getWindowToken()!=null){
//         InputMethodManager  inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//         return inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),0);
//        }
//        return super.onTouchEvent(event);
//    }

    /**
     * 以下两个方法是为了当点击其他地方的时候隐藏键盘
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    // Fly 注： 显示加载 进度条(带文字)、 不带文字、 取消加载进度
     public void showLoading(String msg){
         DialogUtils.showProgress(mActivitySelf, TextUtils.isEmpty(msg)? "正在加载...":msg);
     }

     public void hideLoading(){
        DialogUtils.closeProgress();
     }

     // Fly 注：弹出土司
    public void  showMessage(String msg){
     if(!TextUtils.isEmpty(msg)){
         ToastUtils.showShort(this,msg);
     }
    }

   // Fly 注：自杀
    public void killSelf(){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CtrollAllactivitys.removeActivity(this);
    }
     // Fly 注：返回键返回事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK==keyCode){
            if(mFragmentManager.getBackStackEntryCount()==1){
                finish();
                return  true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    // Fenglingyue 注：  向基础Activity中添加fragment
    public void addFragment( int containId,BaseFragment fragment){
        if(fragment!=null){
            mFragmentManager.beginTransaction()
//                  .setCustomAnimations(  //设置Fragment进入退出动画
//                          R.anim.activity_start_in,
//                          R.anim.activity_start_out,
//                          R.anim.activity_close_in,
//                          R.anim.activity_close_out
//                  )
                    .replace(containId,fragment,fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss(); //这种提交是允许发生异常时状态值丢失的情况下也能正常提交事务
        }
    }

    /*
     *  Fenglingyue 注：获取会退栈中所有事物的数量  大于1的时候 执行回退操作
     *  等于1的时候 表示当前Activity 只剩下一个Fragment ,直接 finish 当前Activity 即可
     */
    public void removeFragment(){
        if(mFragmentManager.getBackStackEntryCount()>1){
            mFragmentManager.popBackStack();
        }else{
            finish();
        }
    }


/*    *//**
     * 隐藏虚拟按键，并且全屏
     *//*
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }*/
}
