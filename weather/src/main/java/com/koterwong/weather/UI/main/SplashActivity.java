package com.koterwong.weather.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.koterwong.weather.R;
import com.koterwong.weather.common.SettingPref;
import com.koterwong.weather.receiver.LocalRegisterHelper;
import com.koterwong.weather.receiver.NotificationReceiver;
import com.koterwong.weather.service.AutoUpdateService;
import com.koterwong.weather.utils.AppUtils;
import com.koterwong.weather.utils.CalendarUtils;
import com.koterwong.weather.utils.ServiceStatueUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SplashActivity extends AppCompatActivity {


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_splash);
        ((TextView)findViewById(R.id.splash_app_name)).setText(AppUtils.getAppName(this));
        ((TextView)findViewById(R.id.splash_app_version)).setText("Version_"+AppUtils.getVersionName(this));
        this.initView();
        this.initData();
        this.copyDB("china_city.db");
    }

    private void initData() {
        if (!ServiceStatueUtils.isServiceRunning(this, "AutoUpdateService")) {
            if (SettingPref.getBoolean(SettingPref.IS_ALLOW_UPDATE, false)) {
                startService(new Intent(this, AutoUpdateService.class));
            }
        }
        if (SettingPref.getBoolean(SettingPref.IS_SHOW_NOTIFY,false)){
            LocalRegisterHelper
                    .getInstance(this)
                    .send(new NotificationReceiver(),LocalRegisterHelper.action_notify);
        }
    }

    private void initView() {
        int resId;
        if (CalendarUtils.isDay()) {
            resId = R.drawable.splash_02;
        }  else {
            resId = R.drawable.splash_01;
        }
        ImageView bgImg = (ImageView) findViewById(R.id.bg_img);
        bgImg.setImageResource(resId);
        AnimationSet set = new AnimationSet(false);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.9f);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,1.2f,1.0f,1.2f,
                ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        set.setDuration(2000);
        set.setFillAfter(true);
        set.addAnimation(alphaAnimation);
        set.addAnimation(scaleAnimation);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {

            }

            @Override public void onAnimationEnd(Animation animation) {
                startActivity();
            }

            @Override public void onAnimationRepeat(Animation animation) {

            }
        });

        bgImg.startAnimation(set);
    }

    private void startActivity(){
        Intent intent = new Intent(SplashActivity.this,MainActivity2.class);
        startActivity(intent);
        finish();
    }

    private void copyDB(String dbName) {
        // 拷贝的目标地址
        File destFile = new File(getFilesDir(), dbName);
        // 判断数据库是否已经存在
        if (destFile.exists()) {
            return;
        }
        FileOutputStream out = null;
        InputStream in = null;
        try {
            in = getAssets().open(dbName);
            out = new FileOutputStream(destFile);
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode){
            return true;
        }else{
            return super.onKeyDown(keyCode, event);
        }
    }
}
