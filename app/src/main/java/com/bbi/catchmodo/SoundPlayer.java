package com.bbi.catchmodo;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class SoundPlayer {

    private AudioAttributes audioAttributes;
    final  int SOUND_POOL_MAX=3;
    private  static SoundPool soundPool;
    private  static  int hitOrangeSound;
    private  static  int hitPinkSound;
    private  static  int hitBlackSound;
    private  static  int sound;
    public SoundPlayer(Context context){

        //API level 21
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_POOL_MAX)
                    .build();

        }else{
            soundPool=new SoundPool(SOUND_POOL_MAX, AudioManager.STREAM_MUSIC,0);
        }
        hitOrangeSound=soundPool.load(context,R.raw.hit,1);
        hitPinkSound=soundPool.load(context,R.raw.hit,1);
        hitBlackSound=soundPool.load(context,R.raw.over,1);
      //  sound=soundPool.load(context,R.raw.over,1);



    }

    public  void  playHitOrangeSound(){
        soundPool.play(hitOrangeSound,.1f,.1f,1,0,1.0f);
    }

/*    public  void  soundAll(){
        soundPool.play(sound,1.0f,1.0f,1,0,1.0f);
    }*/

    public  void  playHitBlackSound(){
        soundPool.play(hitBlackSound,.15f,.15f,1,0,1.0f);
    }
    public  void  playHitPinkSound(){
        soundPool.play(hitPinkSound,.1f,.1f,1,0,1.0f);
    }
}
