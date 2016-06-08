package com.koterwong.weather.ui.main.model;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.koterwong.weather.BaseApplication;

/**
 * Author：Koterwong，Data：2016/5/1.
 * Description:
 * 百度定位SDK
 */
public class MainModel {

    private LocationListener locationListener;

    private LocationClient mLocationClient = null;

    public void locationCity(LocationListener locationListener) {

        this.locationListener = locationListener;

        mLocationClient = new LocationClient(BaseApplication.getApplication());
        BDLocationListener myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setIgnoreKillProcess(false);
        mLocationClient.setLocOption(option);

        mLocationClient.start();
    }

    public interface LocationListener {

        void locationSuccess(String city);

        void locationError();
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            String city = location.getCity();
            if (city != null) {
                if (locationListener != null)
                    locationListener.locationSuccess(city);
            } else {
                if (locationListener != null)
                    locationListener.locationError();
            }
            mLocationClient.stop();
        }
    }
}
