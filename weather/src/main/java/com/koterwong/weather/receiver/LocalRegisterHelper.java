package com.koterwong.weather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * ================================================
 * Created By：Koterwong; Time: 2016/06/11 18:49
 * <p/>
 * Description:
 * =================================================
 */
public class LocalRegisterHelper {

    private Context mContext;
    private static LocalRegisterHelper mInstance;

    public static final String action_notify = "com.koterwong.weather.Notification";

    private LocalRegisterHelper(Context context) {
        this.mContext = context;
    }

    public static LocalRegisterHelper getInstance(Context context) {
        if (mInstance == null) {
            synchronized (LocalRegisterHelper.class) {
                if (mInstance == null) {
                    return mInstance = new LocalRegisterHelper(context);
                }
            }
        }
        return mInstance;
    }

    public void send(BroadcastReceiver receiver, String action) {
        this.register(receiver, action).sendBroadcast(new Intent(LocalRegisterHelper.action_notify));
    }

    public void unRegister(BroadcastReceiver receiver){
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(mContext);
        instance.unregisterReceiver(receiver);
    }

    private LocalBroadcastManager register(BroadcastReceiver receiver, String action) {
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(mContext);
        instance.registerReceiver(receiver, new IntentFilter(action));
        return instance;
    }
}
