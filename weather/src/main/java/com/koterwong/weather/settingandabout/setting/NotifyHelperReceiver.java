package com.koterwong.weather.settingandabout.setting;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.koterwong.weather.R;
import com.koterwong.weather.BaseApplication;
import com.koterwong.weather.beans.WeatherBean;
import com.koterwong.weather.commons.SavedCityDBManager;
import com.koterwong.weather.commons.Setting;
import com.koterwong.weather.main.MainActivity2;
import com.koterwong.weather.weather.WeatherJsonUtil;

/**
 * Author：Koterwong，Data：2016/5/2.
 * Description: 通知栏打开关闭的广播。
 */
public class NotifyHelperReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean isShow = Setting.getBoolean(Setting.IS_SHOW_NOTIFY,false);

        NotificationManager mNotifyMgr = (NotificationManager) BaseApplication.getApplication().
                getSystemService(Context.NOTIFICATION_SERVICE);
        int mNotificationId = 1;
        //取消通知
        mNotifyMgr.cancel(mNotificationId);
        if (!isShow) {
            return;
        }
        //城市和天气数据
        String firstCity = SavedCityDBManager.getInstance(BaseApplication.getApplication()).queryCities().get(0);
        WeatherBean weatherBean = (WeatherBean) BaseApplication.getACache().getAsObject(firstCity);

        String aqi;
        if (weatherBean==null){
            Toast.makeText(BaseApplication.getApplication(),"请连接网络后重试",Toast.LENGTH_SHORT).show();
            return;
        }
        if (weatherBean.aqi != null) {
            aqi = WeatherJsonUtil.getAqi(weatherBean.aqi.city.aqi) + "AQI:" + weatherBean.aqi.city.aqi;
        } else {
            aqi = "";
        }
        //create a  notification Builder
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(BaseApplication.getApplication())
                        .setOngoing(true)
                        .setColor(BaseApplication.getApplication().getResources().getColor(R.color.primary))
//                        .setSubText()
                        .setSmallIcon(WeatherJsonUtil.getWeatherImage(weatherBean.now.cond.txt))
                        .setContentTitle(weatherBean.now.cond.txt)
                        .setContentInfo(firstCity)
                        .setContentText(weatherBean.now.tmp + "°     " + aqi);

        //create a action
        Intent resultIntent = new Intent(BaseApplication.getApplication(), MainActivity2.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                BaseApplication.getApplication(),
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
