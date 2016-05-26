package com.koterwong.weather.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.koterwong.weather.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {

        ImageView bgImg = (ImageView) findViewById(R.id.bg_img);

        AnimationSet set = new AnimationSet(false);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.8f);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,1.1f,1.0f,1.1f,
                ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        set.setDuration(1500);
        set.setFillAfter(true);
        set.addAnimation(alphaAnimation);
        set.addAnimation(scaleAnimation);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        bgImg.startAnimation(set);
    }

    private void startActivity(){
        Intent intent = new Intent(SplashActivity.this,MainActivity2.class);
        startActivity(intent);
//        overridePendingTransition(R.anim.enter,R.anim.exit);
        finish();
    }


}
