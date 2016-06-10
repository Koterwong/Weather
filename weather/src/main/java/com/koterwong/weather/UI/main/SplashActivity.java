package com.koterwong.weather.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koterwong.weather.R;
import com.koterwong.weather.utils.AppUtils;

import java.util.Calendar;

public class SplashActivity extends AppCompatActivity {

    private RelativeLayout mRootView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ((TextView)findViewById(R.id.splash_app_name)).setText(AppUtils.getAppName(this));
        ((TextView)findViewById(R.id.splash_app_version)).setText("Version_"+AppUtils.getVersionName(this));
        initView();
    }

    private void initView() {
        int resId;
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(calendar.HOUR_OF_DAY);
        if (hour > 6 && hour < 18) {
            resId = R.drawable.splash_02;
        }  else {
            resId = R.drawable.splash_01;
        }
        ImageView bgImg = (ImageView) findViewById(R.id.bg_img);
        bgImg.setImageResource(resId);
        mRootView = (RelativeLayout) findViewById(R.id.splash_root_view);
        AnimationSet set = new AnimationSet(false);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.9f);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,1.2f,1.0f,1.2f,
                ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        set.setDuration(2000);
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
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(
               this,mRootView,"");
        ActivityCompat.startActivity(this,intent,compat.toBundle());
        finish();
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode){
            return true;
        }else{
            return super.onKeyDown(keyCode, event);
        }
    }
}
