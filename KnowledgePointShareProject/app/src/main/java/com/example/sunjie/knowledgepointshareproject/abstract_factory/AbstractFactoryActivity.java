package com.example.sunjie.knowledgepointshareproject.abstract_factory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunjie.knowledgepointshareproject.R;

public class AbstractFactoryActivity extends AppCompatActivity {

    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abstract_factory);
        tv = (TextView) findViewById(R.id.tv);

    }

    public void go1(View view) {
        PhoneAbstractFactory xiaoMiPhone = new XiaoMiPhoneFactory();
        xiaoMiPhone.installCamera().takePhoto();
        xiaoMiPhone.installScreen().look();
    }

    public void go2(View view) {
        PhoneAbstractFactory hwPhone = new HWPhoneFactory();
        hwPhone.installScreen().look();
        hwPhone.installCamera().takePhoto();
    }

    public void go3(View view) {
        Toast.makeText(this, "触发go3", Toast.LENGTH_SHORT).show();
    }

    public void go4(View view) {
        Toast.makeText(this, "触发go4", Toast.LENGTH_SHORT).show();
    }
}
