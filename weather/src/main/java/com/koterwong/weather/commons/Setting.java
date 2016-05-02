package com.koterwong.weather.commons;

import android.content.Context;
import android.content.SharedPreferences;

import com.koterwong.weather.base.BaseApplication;

/**
 * Author：Koterwong，Data：2016/4/27.
 * Description:
 */
public class Setting {

    public static final String CACHE_NAME = "Weather_Cache";

    public static final String IS_ALLOW_LOCATION = "isAllowLocation";
    public static final String IS_ALLOW_UPDATE = "isAllowUpdate";
    public static final String AUTO_UPDATE_TIME = "autoUpdateTime";

    private static SharedPreferences mPref =
            BaseApplication.getApplication().getSharedPreferences("config", Context.MODE_PRIVATE);

    public static void putString(String key, String value){
        mPref.edit().putString(key,value).apply();
    }
    public static String getString (String key,String defaultValue){
        return mPref.getString(key,defaultValue);
    }

    public static void putBoolean(String key, boolean value){
        mPref.edit().putBoolean(key,value).apply();
    }
    public static boolean getBoolean (String key,boolean defaultValue){
        return mPref.getBoolean(key,defaultValue);
    }

}
