package com.koterwong.weather.settingandabout.setting;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.koterwong.weather.BaseApplication;
import com.koterwong.weather.commons.SavedCityDBManager;
import com.koterwong.weather.commons.Setting;
import com.koterwong.weather.utils.L;
import com.koterwong.weather.weather.model.WeatherModelImp;

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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        L.d("onCreate");
        mTask = new IntervalTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.d("onStartCommand");
        if (!Setting.getBoolean(Setting.IS_ALLOW_UPDATE, false)) {
            /**
             * 需要注意的是，即使调用了stopSelf方法，服务仍会接着执行完onStartCommand。
             * 这对于本次的定时任务并不适用。
             */
            stopSelf();
        }
        int timeHour = Integer.parseInt(Setting.getString(Setting.AUTO_UPDATE_TIME,"8"));
        //RxJava执行周期性任务
        mObservable = Observable.interval(timeHour, TimeUnit.HOURS);
        //订阅任务
        mSubscribe = mObservable.subscribe(mTask);
        return START_REDELIVER_INTENT;
    }

    class IntervalTask implements Observer<Long> {

        @Override
        public void onCompleted() {
            L.d("service："+"onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            L.d("service："+"onError");
        }

        @Override
        public void onNext(Long aLong) {
            //执行网络操作，请求天气数据
            loadWeather();
        }
    }


    @Override
    public void onDestroy() {
        L.d("onDestroy");
        /**
         * 取消订阅的周期性任务
         */
        if (!mSubscribe.isUnsubscribed()){
            L.d("isUnsubscribed","true");
            mSubscribe.unsubscribe();
        }
        super.onDestroy();
    }

    private void loadWeather() {
        List<String> cityList = SavedCityDBManager.getInstance(BaseApplication.getApplication())
                .queryCities();
        final WeatherModelImp weatherModelImp = new WeatherModelImp();
        //RxJava遍历数组，List
        Observable
                .from(cityList)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        L.d("后台更新的城市",s);
                        /**
                         * 重新请求数据保存到本地
                         */
                        weatherModelImp.loadWeatherFromServer(s, null);
                    }
                });
    }
}
