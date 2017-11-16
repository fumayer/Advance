package com.shortmeet.www.ui.PercenalCenter.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shortmeet.www.Base.BaseActivity;
import com.shortmeet.www.R;
import com.shortmeet.www.mvp.IMVPView;
import com.shortmeet.www.mvp.IMVPrecenter;

public class PrivateActivity extends BaseActivity implements IMVPView, View.OnClickListener {
    //跟布局
    private LinearLayout activityPrivate;
    //允许我关注的人评论我
    private LinearLayout linearAllowothericareCommen;
    private ImageView imgvAllowOthercommen;
    //允许关注者发私聊给我
    private LinearLayout linearAllowotherMessageme;
    private ImageView imgvOthermessgeme;
    //允许其他用户查看我关注的人与我的粉丝
    private LinearLayout linearOtherlookme;
    private ImageView imgvOtherlookeme;
    //黑名单
    private LinearLayout linearBlackNamelist;
    private IMVPrecenter  mPrecenter;
    /*
     *  Fly 注：boolean
     */
    private boolean allowothericarecommen_isOpen;
    private boolean othermessgeme_isOpen;
    private boolean otherlookeme_isOpen;

    @Override
    public int setRootView() {
        return R.layout.activity_private;
    }

    @Override
    public boolean setUseTitleBar() {
        return true;
    }

    @Override
    public void initView() {
    mPrecenter=new IMVPrecenter(this);
    tvCenterTitleBar.setText("隐私");
    imgvLeftbackTitlebar.setVisibility(View.VISIBLE);
    activityPrivate = (LinearLayout) findViewById(R.id.activity_private);
    linearAllowothericareCommen = (LinearLayout) findViewById(R.id.linear_allowothericare_commen);
    imgvAllowOthercommen = (ImageView) findViewById(R.id.imgv_allow_othercommen);
    linearAllowotherMessageme = (LinearLayout) findViewById(R.id.linear_allowother_messageme);
    imgvOthermessgeme = (ImageView) findViewById(R.id.imgv_othermessgeme);
    linearOtherlookme = (LinearLayout) findViewById(R.id.linear_otherlookme);
    imgvOtherlookeme = (ImageView) findViewById(R.id.imgv_otherlookeme);
    linearBlackNamelist = (LinearLayout) findViewById(R.id.linear_black_namelist);
    }
    @Override
    public void initListener() {
    titleBarLeftframelayout.setOnClickListener(this);
    linearAllowothericareCommen.setOnClickListener(this);
    linearAllowotherMessageme.setOnClickListener(this);
    linearOtherlookme.setOnClickListener(this);
    linearBlackNamelist.setOnClickListener(this);
    }
    @Override
    public void initData() {

    }

    @Override
    public void setData(Object o, int id) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_leftframelayout:
                   finish();
                break;
            case R.id.linear_allowothericare_commen: //允许我关注的人评论我
             if(allowothericarecommen_isOpen){
              imgvAllowOthercommen.setImageResource(R.mipmap.bt_close);
             }else{
               imgvAllowOthercommen.setImageResource(R.mipmap.bt_open);
             }
                allowothericarecommen_isOpen=!allowothericarecommen_isOpen;
                break;
            case R.id.linear_allowother_messageme: //允许关注者发私聊给我
                if(othermessgeme_isOpen){
                    imgvOthermessgeme.setImageResource(R.mipmap.bt_close);
                }else{
                    imgvOthermessgeme.setImageResource(R.mipmap.bt_open);
                }
                othermessgeme_isOpen=!othermessgeme_isOpen;
                break;
            case  R.id.linear_otherlookme://允许其他用户查看我关注的人与我的粉丝
                if(otherlookeme_isOpen){
                    imgvOtherlookeme.setImageResource(R.mipmap.bt_close);
                }else{
                    imgvOtherlookeme.setImageResource(R.mipmap.bt_open);
                }
                otherlookeme_isOpen=!otherlookeme_isOpen;
                break;
            case R.id.linear_black_namelist://黑名单

                break;
        }
    }
}
