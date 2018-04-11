package com.example.sj.app2;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayUtils {
    // SoundPool对象
    public static SoundPool mSoundPlayer = new SoundPool(10,
            AudioManager.STREAM_MUSIC, 5);
    public static SoundPlayUtils soundPlayUtils;

    public static enum MusicType {
        GAME_SUCCESS(1),
        GAME_FAIL(2),
        GO(3),
        OPER(4),
        READY_GO(5),
        BUY_BALL(6),   //买球
        SEND_BALL(7),  //发球
        ECPTOMA_BALL(8),  //落球
        EAT_BALL(9),   //吃球
        SHOT(10),      //投篮
        ADVANCE(11),  //前进
        NEW_GAME_REWARD(12),  //积分奖励面板
        DOWN_MACHINE(13),     //下机
        TIME_COUNT_DOWN(14);   //倒计时
        private int value;

        private MusicType(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    /**
     * 初始化
     *
     * @param context
     */
    public static SoundPlayUtils init(Context context) {
        if (soundPlayUtils == null) {
            soundPlayUtils = new SoundPlayUtils();

            mSoundPlayer.load(context, R.raw.game_success, 1);// 1
            mSoundPlayer.load(context, R.raw.game_fail, 1);// 2
            mSoundPlayer.load(context, R.raw.go, 1);// 3
            mSoundPlayer.load(context, R.raw.oper, 1);// 4
            mSoundPlayer.load(context, R.raw.ready_go, 1);//5
            mSoundPlayer.load(context, R.raw.buy_ball, 1);//6
            mSoundPlayer.load(context, R.raw.send_ball, 1);//7
            mSoundPlayer.load(context, R.raw.ecptoma_ball, 1);//8
            mSoundPlayer.load(context, R.raw.eat_ball, 1);//9
            mSoundPlayer.load(context, R.raw.shot, 1);//10
            mSoundPlayer.load(context, R.raw.advance, 1);//11
            mSoundPlayer.load(context, R.raw.new_game_reward, 1);//12
            mSoundPlayer.load(context, R.raw.down_machine, 1);//13
            mSoundPlayer.load(context, R.raw.time_count_down, 1);//14
        }
        
        return soundPlayUtils;
    }

    /**
     * 播放声音
     *
     * @param soundID
     */
    public static void play(int soundID) {
        mSoundPlayer.play(soundID, 1, 1, 0, 0, 1);
    }

    public static void stop(){
        if (mSoundPlayer != null){
            mSoundPlayer.autoPause();
        }
    }
}
