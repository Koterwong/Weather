package com.koterwong.weather;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.koterwong.weather.utils.ACache;
import com.koterwong.weather.utils.LogUtils;

import im.fir.sdk.FIR;

/**
 * Author：Koterwong，Data：2016/4/24.
 * Description:
 */
public class MyApp extends Application {

    public static final String CACHE_NAME = "Weather_Cache";
    private static Handler mHandler;
    private static int mainId;
    private static MyApp mInstance;
    private static ACache mACache;

    @Override public void onCreate() {
        super.onCreate();
        mInstance=this;
        mainId = android.os.Process.myTid();
        mHandler = new Handler();
        mACache = ACache.get(this, CACHE_NAME);
        LogUtils.isDebug = false;  //release版本不打印log
        FIR.init(this);  //新版本检查更新。
    }

    /**
     * 返回application对象
     */
    public static Context getApp() {
        return mInstance;
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
