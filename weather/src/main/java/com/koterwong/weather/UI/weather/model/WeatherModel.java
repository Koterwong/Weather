package com.koterwong.weather.ui.weather.model;

/**
 * 作者：Koterwong，创建日期：2016/4/24.
 * Description:
 */
public interface WeatherModel {

    void loadWeatherFromServer(String cityName, WeatherModelImp.LoadServiceListener listener);

    void loadLocation(String cityName, WeatherModelImp.LoadLocationListener listener);
}
