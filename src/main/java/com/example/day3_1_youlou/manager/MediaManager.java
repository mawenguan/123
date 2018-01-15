package com.example.day3_1_youlou.manager;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by mwg on 2017/12/7.
 */

public class MediaManager {

    public static SoundPool soundPool = null;

    /**
     * 控制声音的播放
     * @param context
     * @param resId   要播放的声音的资源ID
     */
    public static void playMusic(Context context, int resId) {
        if (soundPool == null) {
            soundPool = new SoundPool(4,
                    AudioManager.STREAM_MUSIC,0);
        }
        // 给音效池设置一个音乐加载的监听器
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                // 该方法回调，说明音乐加载完毕可以进行播放了
                soundPool.play(sampleId,1.0f,
                        1.0f,1,0,1.0f );
            }
        });
        // 向音效池里面加载音乐
        soundPool.load(context,resId,1);
    }

    public static void release(){
        if (soundPool!=null){
            soundPool.release();
            soundPool=null;
        }
    }
}
