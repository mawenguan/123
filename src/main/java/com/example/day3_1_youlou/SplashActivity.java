package com.example.day3_1_youlou;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.lang.annotation.Annotation;

public class SplashActivity extends Activity {

    ImageView imageView_Splash;
    Animation animation_Splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //初始化控件
        imageView_Splash = findViewById(R.id.imageView_Splash);
        //加载动画
        animation_Splash = AnimationUtils.loadAnimation(
                this,R.anim.splash);
        //把动画设置到控件上
        imageView_Splash.setAnimation(animation_Splash);
        animation_Splash.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                //实现跳转时的一个切换动画
                overridePendingTransition(
                        R.anim.enter_anim,
                        R.anim.out_anim);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
