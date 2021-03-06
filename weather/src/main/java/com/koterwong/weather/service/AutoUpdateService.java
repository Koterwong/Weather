package com.koterwong.weather.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.koterwong.weather.MyApp;
import com.koterwong.weather.common.database.SavedCityDBManager;
import com.koterwong.weather.common.SettingPref;
import com.koterwong.weather.utils.LogUtils;
import com.koterwong.weather.ui.weather.model.WeatherModelImp;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;

public class AutoUpdateService extends Service {

    private Observable<Long> mObservable;
    private IntervalTask mTask;
    private Subscription mSubscribe;

    @Override public IBinder onBind(Intent intent) {
        return null;
    }


    @Override public void onCreate() {
        LogUtils.d("onCreate");
        mTask = new IntervalTask();
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        if (!SettingPref.getBoolean(SettingPref.IS_ALLOW_UPDATE, false)) {
            /**
             * 需要注意的是，即使调用了stopSelf方法，服务仍会接着执行完onStartCommand。
             * 这对于本次的定时任务并不适用。
             */
            stopSelf();
        }
        int timeHour = Integer.parseInt(SettingPref.getString(SettingPref.AUTO_UPDATE_TIME,"8"));
        //RxJava执行周期性任务
        mObservable = Observable.interval(timeHour, TimeUnit.HOURS);
        //订阅任务
        mSubscribe = mObservable.subscribe(mTask);
        return START_REDELIVER_INTENT;
    }

    class IntervalTask implements Observer<Long> {

        @Override public void onCompleted() {
            LogUtils.d("service："+"onCompleted");
        }

        @Override public void onError(Throwable e) {
            LogUtils.d("service："+"onError");
        }

        @Override public void onNext(Long aLong) {
            //执行网络操作，请求天气数据
            AutoUpdateService.this.loadWeather();
        }
    }


    @Override public void onDestroy() {
        LogUtils.d("onDestroy");
        if (!mSubscribe.isUnsubscribed()){
            mSubscribe.unsubscribe();
        }
        super.onDestroy();
    }

    private void loadWeather() {
        List<String> cityList = SavedCityDBManager.getInstance(MyApp.getApp()).queryCities();
        final WeatherModelImp weatherModelImp = new WeatherModelImp();
        Observable.from(cityList)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        weatherModelImp.loadWeatherFromServer(s, null);
                    }
                });
    }
}
