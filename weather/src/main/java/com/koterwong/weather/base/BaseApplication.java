package com.koterwong.weather.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.koterwong.weather.utils.ACache;

/**
 * Author：Koterwong，Data：2016/4/24.
 * Description:
 */
public class BaseApplication extends Application {

    public static final String CACHE_NAME = "Weather_Cache";

    private static Handler mHandler;

    private static int mainId;

    private static BaseApplication application;

    private static ACache mACache;

    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
        mainId = android.os.Process.myTid();
        mHandler = new Handler();
        mACache = ACache.get(this, CACHE_NAME);
    }

    /**
     * 返回application对象
     */
    public static Context getApplication() {
        return application;
    }

    /**
     * 获取主线程的id
     */
    public static int getMainId(){
        return  mainId;
    }
    /**
     * 获取handler对象
     */
    public static Handler getHandler(){
        return mHandler;
    }

    public static ACache getACache() {
        return mACache;
    }
}
