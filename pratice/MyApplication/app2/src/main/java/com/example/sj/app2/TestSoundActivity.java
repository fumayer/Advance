package com.example.sj.app2;

import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class TestSoundActivity extends AppCompatActivity {

    private SoundPool soundPool;
    private int soundId;
    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_sound);
        tv = (TextView) findViewById(R.id.tv);
        tv.setSoundEffectsEnabled(false);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.performHapticFeedback(HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
                tv.playSoundEffect(SoundEffectConstants.CLICK);
            }
        });
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().build();
        } else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        soundId = soundPool.load(this, R.raw.game_fail, 1);
    }


    public void go1(View view) {
        Toast.makeText(this, "触发go1", Toast.LENGTH_SHORT).show();
        playSound();
    }

    public void go2(View view) {
        Toast.makeText(this, "触发go2", Toast.LENGTH_SHORT).show();
        playSound();

    }

    public void go3(View view) {
        Toast.makeText(this, "触发go3", Toast.LENGTH_SHORT).show();
        view.setSoundEffectsEnabled(true);
        view.playSoundEffect(SoundEffectConstants.CLICK);
        SoundPlayUtils.init(this);
    }

    public void go4(View view) {
        SoundPlayUtils.play(SoundPlayUtils.MusicType.OPER.getValue());

    }

    private void playSound() {
        soundPool.play(
                soundId,
                0.1f,   //左耳道音量【0~1】
                0.5f,   //右耳道音量【0~1】
                0,     //播放优先级【0表示最低优先级】
                0,     //循环模式【0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次数】
                1     //播放速度【1是正常，范围从0~2】
        );
    }
}
