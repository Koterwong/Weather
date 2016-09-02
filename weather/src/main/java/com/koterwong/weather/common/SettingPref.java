package com.koterwong.weather.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.koterwong.weather.MyApp;

/**
 * Author：Koterwong，Data：2016/4/27.
 * Description:
 */
public class SettingPref {

    public static final String IS_ALLOW_LOCATION = "isAllowLocation";
    public static final String IS_ALLOW_UPDATE = "isAllowUpdate";
    public static final String IS_SHOW_NOTIFY = "isShowNotify";
    public static final String AUTO_UPDATE_TIME = "autoUpdateTime";
    public static final String AUTO_DELETE_CACHE_TIME = "autoDeleteCacheTime";
    public static final String IS_HAS_NEW_VERSION = "isHasNewVersion";

    public static void putString(String key, String value) {
        SharedPreferences mPref = MyApp.getApp().getSharedPreferences("config", Context.MODE_PRIVATE);
        mPref.edit().putString(key, value).apply();
    }

    public static String getString(String key, String defaultValue) {
        SharedPreferences mPref = MyApp.getApp().getSharedPreferences("config", Context.MODE_PRIVATE);
        return mPref.getString(key, defaultValue);
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences mPref = MyApp.getApp().getSharedPreferences("config", Context.MODE_PRIVATE);
        mPref.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences mPref = MyApp.getApp().getSharedPreferences("config", Context.MODE_PRIVATE);
        return mPref.getBoolean(key, defaultValue);
    }

}
