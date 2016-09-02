package com.koterwong.weather.ui.managercity.presenter;

import com.koterwong.weather.MyApp;
import com.koterwong.weather.bean.WeatherBean;
import com.koterwong.weather.common.database.SavedCityDBManager;
import com.koterwong.weather.ui.managercity.view.ManagerCityView;
import com.koterwong.weather.ui.weather.WeatherJsonUtil;

import java.util.List;

/**
 * Author：Koterwong，Data：2016/4/29.
 * Description:
 */
public class ManagerCityPresenterImpl implements ManagerCityPresenter{

    private ManagerCityView mView;

    public ManagerCityPresenterImpl(ManagerCityView mView) {
        this.mView = mView;
    }

    @Override public List<String> querySavedCityList(){
       return SavedCityDBManager
               .getInstance(MyApp.getApp())
               .queryCities();
    }

    @Override public void deleteCity(String cityName){
        SavedCityDBManager
                .getInstance(MyApp.getApp())
                .deleteCity(cityName);
    }

    @Override public WeatherBean.NowBean querySimpleWeather(String city){
        WeatherBean weatherBean = WeatherJsonUtil.getLocWeatherBean(city);
        if (weatherBean!=null){
            return weatherBean.now;
        }else{
            return null;
        }
    }
}
