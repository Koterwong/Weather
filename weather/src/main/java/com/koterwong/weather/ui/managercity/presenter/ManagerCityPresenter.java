package com.koterwong.weather.ui.managercity.presenter;

import com.koterwong.weather.beans.WeatherBean;

import java.util.List;

/**
 * ================================================
 * Created By：Koterwong; Time: 2016/06/14 21:09
 * <p/>
 * Description:
 * =================================================
 */
public interface ManagerCityPresenter {

    List<String> querySavedCityList();

    void deleteCity(String cityName);

    WeatherBean.NowBean querySimpleWeather(String city);
}
