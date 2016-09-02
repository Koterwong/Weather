package com.koterwong.weather.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Author：Koterwong，Data：2016/5/1.
 * Description:判断服务是否在运行。
 */
public class ServiceStatueUtils {

    public static boolean isServiceRunning(Context context, String serviceName) {

        ActivityManager am = (ActivityManager) context.getSystemService
                (Context.ACTIVITY_SERVICE);
        // 获取系统所有正在运行的服务,最多返回100个
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices) {
            // 获取服务的名称
            String className = runningServiceInfo.service.getClassName();
            if (className.equals(serviceName)) {
                return true;
            }
        }
        return false;
    }
}
