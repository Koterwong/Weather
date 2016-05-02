package com.koterwong.weather.weather.view;

import com.koterwong.weather.beans.WeatherBean;

import java.util.List;

/**
 * 作者：Koterwong，创建日期：2016/4/24.
 * Description:
 */
public interface WeatherView {

    void showToastMsg(String msg);

    void setContentVisible(boolean statue);

    void setLoadEmpty();

    void setmSwipeRefreshLayoutStatue(boolean statue);

    boolean isSwipeRefreshLayoutRefreshing();

    void setmWeatherIcon(int ResId);

    void setmWeatherDes(String mWeatherDes);

    void setmWeatherTmp(String mWeatherTmp);

    void setmWeatherAqi(String mWeatherAqi);

    void setmWeatherHum(String mWeatherHum);

    void setmWeatherWind(String mWeatherWind);

    void setmWeatherUpdateTime(String mWeatherUpdateTime);

    void refreshWeatherDaily(List<WeatherBean.DailyForecastBean> mDailyForecastList);
}
