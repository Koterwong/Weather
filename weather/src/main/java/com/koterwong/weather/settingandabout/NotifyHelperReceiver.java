package com.koterwong.weather.settingandabout;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.koterwong.weather.base.BaseApplication;
import com.koterwong.weather.beans.WeatherBean;
import com.koterwong.weather.commons.SavedCityDBManager;
import com.koterwong.weather.main.MainActivity2;
import com.koterwong.weather.weather.WeatherJsonUtil;

/**
 * Author：Koterwong，Data：2016/5/2.
 * Description: 通知栏打开关闭的广播。
 */
public class NotifyHelperReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean isShow = intent.getBooleanExtra("isShow", false);

        NotificationManager mNotifyMgr = (NotificationManager) BaseApplication.getApplication().
                getSystemService(Context.NOTIFICATION_SERVICE);
        int mNotificationId = 1;
        //取消通知
        mNotifyMgr.cancel(mNotificationId);
        if (!isShow){
           return;
        }
        //城市和天气数据
        String firstCity = SavedCityDBManager.getInstance(BaseApplication.getApplication()).queryCities().get(0);
        WeatherBean weatherBean = (WeatherBean) BaseApplication.getACache().getAsObject(firstCity);

        //create a  notification Builder
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(BaseApplication.getApplication())
                        .setOngoing(true)
                        .setSmallIcon(WeatherJsonUtil.getWeatherImage(weatherBean.now.cond.txt))
                        .setContentTitle(firstCity)
                        .setContentText(weatherBean.now.cond.txt+"  "+weatherBean.now.tmp+"°");

        //create a action
        Intent resultIntent = new Intent(BaseApplication.getApplication(), MainActivity2.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                BaseApplication.getApplication(),
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT // no need to create an artificial back stack.
        );
        mBuilder.setContentIntent(resultPendingIntent);

        //issue the notification
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
