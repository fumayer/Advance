package com.example.testnavigationbaractivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.QuickContactBadge;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private QuickContactBadge mQcb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQcb = (QuickContactBadge) findViewById(R.id.qcb);
//        mQcb.assignContactFromEmail("winuxxan@gmail.com", true);
        mQcb.assignContactFromPhone("110", true);
//        mQcb.setMode(ContactsContract.QuickContact.MODE_LARGE);
        mQcb.setMode(ContactsContract.QuickContact.MODE_SMALL);
//        mQcb.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qcb:
                Toast.makeText(this, "触发", Toast.LENGTH_LONG);
                break;
        }
    }

    public void go1(View view) {
        Toast.makeText(this, "触发go1", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, NavigatorBarActivity.class));
    }

    public void go2(View view) {
        Toast.makeText(this, "触发go2", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, NavigaActivity.class));

    }

    public void go3(View view) {
        Toast.makeText(this, "触发go3", Toast.LENGTH_SHORT).show();
    }

    public void go4(View view) {
        Toast.makeText(this, "触发go4", Toast.LENGTH_SHORT).show();
    }

    public void go5(View view) {
        Toast.makeText(this, "触发go5", Toast.LENGTH_SHORT).show();
    }

    public void go6(View view) {
        Toast.makeText(this, "触发go6", Toast.LENGTH_SHORT).show();
    }

    public void go7(View view) {
        Toast.makeText(this, "触发go7", Toast.LENGTH_SHORT).show();
    }

    public void go8(View view) {
        Toast.makeText(this, "触发go8", Toast.LENGTH_SHORT).show();
    }

}
