package com.koterwong.weather.weather.model;

/**
 * 作者：Koterwong，创建日期：2016/4/24.
 * Description:
 */
public interface WeatherModel {

    /* 包含的业务逻辑 */
    void loadWeatherFromServer(String cityName,WeatherModelImp.LoadServiceListener listener);
    void loadLocation(String cityName,WeatherModelImp.LoadLocationListener listener);

}
