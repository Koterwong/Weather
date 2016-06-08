package com.koterwong.weather.commons;

import android.content.Context;
import android.content.SharedPreferences;

import com.koterwong.weather.BaseApplication;

/**
 * Author：Koterwong，Data：2016/4/27.
 * Description:
 */
public class Setting {

    public static final String IS_ALLOW_LOCATION = "isAllowLocation";
    public static final String IS_ALLOW_UPDATE = "isAllowUpdate";
    public static String IS_SHOW_NOTIFY = "isShowNotify";
    public static final String AUTO_UPDATE_TIME = "autoUpdateTime";
    public static String AUTO_DELETE_CACHE_TIME = "autoDeleteCacheTime";

    public static void putString(String key, String value) {
        SharedPreferences mPref = BaseApplication.getApplication().getSharedPreferences("config", Context.MODE_PRIVATE);
        mPref.edit().putString(key, value).apply();
    }

    public static String getString(String key, String defaultValue) {
        SharedPreferences mPref = BaseApplication.getApplication().getSharedPreferences("config", Context.MODE_PRIVATE);
        return mPref.getString(key, defaultValue);
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences mPref = BaseApplication.getApplication().getSharedPreferences("config", Context.MODE_PRIVATE);
        mPref.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences mPref = BaseApplication.getApplication().getSharedPreferences("config", Context.MODE_PRIVATE);
        return mPref.getBoolean(key, defaultValue);
    }

    public void test(){

    }

}
