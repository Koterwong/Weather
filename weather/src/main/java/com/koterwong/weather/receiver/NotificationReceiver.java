package com.koterwong.weather.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.koterwong.weather.R;
import com.koterwong.weather.MyApp;
import com.koterwong.weather.bean.WeatherBean;
import com.koterwong.weather.common.database.SavedCityDBManager;
import com.koterwong.weather.common.SettingPref;
import com.koterwong.weather.ui.main.MainActivity2;
import com.koterwong.weather.ui.weather.WeatherJsonUtil;

/**
 * Author：Koterwong，Data：2016/5/2.
 * Description: 通知栏打开关闭的广播。
 */
public class NotificationReceiver extends BroadcastReceiver {

    @Override public void onReceive(Context context, Intent intent) {

        boolean isShow = SettingPref.getBoolean(SettingPref.IS_SHOW_NOTIFY,false);

        NotificationManager mNotifyMgr = (NotificationManager) MyApp.getApp() .
                getSystemService(Context.NOTIFICATION_SERVICE);
        int mNotificationId = 1;
        //取消通知
        mNotifyMgr.cancel(mNotificationId);
        if (!isShow) {
            return;
        }
        //城市和天气数据
        String firstCity = SavedCityDBManager.getInstance(MyApp.getApp()).queryCities().get(0);
        WeatherBean weatherBean = (WeatherBean) MyApp.getACache().getAsObject(firstCity);

        if (weatherBean==null){
            Toast.makeText(MyApp.getApp(),"请连接网络后重试",Toast.LENGTH_SHORT).show();
            return;
        }
        //create a  notification Builder
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(MyApp.getApp())
                        .setOngoing(true)
                        .setColor(MyApp.getApp().getResources().getColor(R.color.primary))
//                        .setSubText()
                        .setSmallIcon(WeatherJsonUtil.getWeatherImage(weatherBean.now.cond.txt))
                        .setContentTitle(firstCity)
                        .setContentText(weatherBean.now.cond.txt+"  "+weatherBean.now.tmp+"°");

        //create a action
        Intent resultIntent = new Intent(MyApp.getApp(), MainActivity2.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                MyApp.getApp(),
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT // no need to create an artificial back stack.
        );
        mBuilder.setContentIntent(resultPendingIntent);

        Notification build = mBuilder.build();
        int largeIcon = WeatherJsonUtil.getWeatherImage(weatherBean.now.cond.txt);
        build.contentView.setImageViewResource(android.R.id.icon, largeIcon);
        //issue the notification
        mNotifyMgr.notify(mNotificationId, build);
    }
}
