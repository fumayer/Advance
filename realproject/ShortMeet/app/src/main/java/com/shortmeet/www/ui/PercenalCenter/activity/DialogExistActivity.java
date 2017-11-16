package com.shortmeet.www.ui.PercenalCenter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shortmeet.www.Base.CtrollAllactivitys;
import com.shortmeet.www.MainActivity;
import com.shortmeet.www.R;
import com.shortmeet.www.utilsUsed.LogUtils;
import com.shortmeet.www.utilsUsed.UserUtils;
/*
 *  Fly 注： 用户身份过期 得对话框
 */
public class DialogExistActivity extends Activity {
    private TextView tvTitle;
    private TextView tvMessage;
    private Button btnNegative;
    private Button btnPositive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_exist);
        initView();
    }
    public void initView(){
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        btnNegative = (Button) findViewById(R.id.btn_negative);
        btnPositive = (Button) findViewById(R.id.btn_positive);
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doActExist();
            }
        });
    }
    public void doActExist(){
        UserUtils.clearUser(this);
        startActivity(new Intent(this,LoginActivity.class));
        this.finish();
        CtrollAllactivitys.getActivity(MainActivity.class).finish();
        LogUtils.e("","清空用户信息啦");
    }
}
