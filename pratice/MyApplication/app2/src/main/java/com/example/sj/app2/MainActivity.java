package com.example.sj.app2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.sj.app2.arc.TestArcActivity;
import com.example.sj.app2.fragmentconn.TestFragmentConnActivity;
import com.example.sj.app2.popup.TestPopupActivity;
import com.example.sj.app2.seekbar.SeekBarActivity;
import com.example.sj.app2.seekbar.Seekbar2Activity;
import com.example.sj.app2.seekbar.TestCanvasRotateActivity;
import com.example.sj.app2.sync.TestSyncActivity;
import com.example.sj.app2.toast.TestToastActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void go1(View view) {
        Toast.makeText(this, "触发go1", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestMathActivity.class));
    }

    public void go2(View view) {
        Toast.makeText(this, "触发go2", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestCrashActivity.class));
    }

    public void go3(View view) {
        Toast.makeText(this, "触发go3", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestInstanceActivity.class));

    }

    public void go4(View view) {
        Toast.makeText(this, "触发go4", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestListActivity.class));

    }

    public void go5(View view) {
        Toast.makeText(this, "触发go5", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestLinkedHashMapActivity.class));
    }

    public void go6(View view) {
        Toast.makeText(this, "触发go6", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestAnimActivity.class));

    }

    public void go7(View view) {
        Toast.makeText(this, "触发go7", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestArcActivity.class));

    }

    public void go8(View view) {
        Toast.makeText(this, "触发go8", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestSwitchActivity.class));

    }

    public void go9(View view) {
        Toast.makeText(this, "触发go9", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestSyncActivity.class));
    }

    public void go10(View view) {
        Toast.makeText(this, "触发go10", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestAppListActivity.class));

    }

    public void go11(View view) {
        Toast.makeText(this, "触发go11", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, UnitConversionActivity.class));

    }

    public void go12(View view) {
        Toast.makeText(this, "触发go12", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestPopupActivity.class));

    }

    public void go13(View view) {
        Toast.makeText(this, "触发go13", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, SeekBarActivity.class));

    }

    public void go14(View view) {
        Toast.makeText(this, "触发go14", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestCanvasRotateActivity.class));

    }

    public void go15(View view) {
        Toast.makeText(this, "触发go15", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, Seekbar2Activity.class));

    }

    public void go16(View view) {
        Toast.makeText(this, "触发go16", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestToastActivity.class));


    }

    public void go17(View view) {
        Toast.makeText(this, "触发go17", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestFragmentConnActivity.class));
    }

    public void go18(View view) {
        Toast.makeText(this, "触发go18", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestNewViewActivity.class));

    }

    public void go19(View view) {
        Toast.makeText(this, "触发go19", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, ColorFilterActivity.class));

    }

    public void go20(View view) {
        Toast.makeText(this, "触发go20", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestCommonItemActivity.class));

    }

    public void go21(View view) {
        Toast.makeText(this, "触发go21", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestSoundActivity.class));

    }

    public void go22(View view) {
        Toast.makeText(this, "触发go22", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, TestListViewActivity.class));
    }

    public void go23(View view) {
        Toast.makeText(this, "触发go23", Toast.LENGTH_SHORT).show();
    }

    public void go24(View view) {
        Toast.makeText(this, "触发go24", Toast.LENGTH_SHORT).show();
    }

}
