package com.koterwong.weather.settingandabout;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.koterwong.weather.base.BaseApplication;
import com.koterwong.weather.commons.SavedCityDBManager;
import com.koterwong.weather.commons.Setting;
import com.koterwong.weather.utils.L;
import com.koterwong.weather.weather.model.WeatherModelImp;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

public class AutoUpdateService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        if (!Setting.getBoolean(Setting.IS_ALLOW_UPDATE, false)) {
            //如果不允许自动更新，停掉自己。
            stopSelf();
        }
        int timeHour = Integer.parseInt(Setting.getString(Setting.AUTO_UPDATE_TIME, "0"));
        //RxJava执行定时任务
        Observable.interval(timeHour, TimeUnit.HOURS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        //执行网络操作，请求天气数据
                        loadWeather();
                    }
                });
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (Setting.getBoolean(Setting.IS_ALLOW_UPDATE,false)){
//            //如果不允许自动更新，停掉自己。
//            stopSelf();
//        }
//        int timeHour = Integer.parseInt(Setting.getString(Setting.AUTO_UPDATE_TIME,"0"));
//        //RxJava执行定时任务
//        Observable.interval(timeHour, TimeUnit.MINUTES)
//                .subscribe(new Observer<Long>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(Long aLong) {
//                        //执行网络操作，请求天气数据
//                       loadWeather();
//                    }
//                });
//        return super.onStartCommand(intent, flags, startId);
//    }

    private void loadWeather() {
        List<String> cityList = SavedCityDBManager.getInstance(BaseApplication.getApplication())
                .queryCities();
        final WeatherModelImp weatherModelImp = new WeatherModelImp();
        //RxJava遍历数据，List
        Observable
                .from(cityList)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        L.e(s);
                        /**
                         * 重新请求数据保存到本地
                         */
                        weatherModelImp.loadWeatherFromServer(s, null);
                    }
                });
    }
}
