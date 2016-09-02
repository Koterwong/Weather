package com.koterwong.weather.ui.weather.view;

import com.koterwong.weather.bean.WeatherBean;

import java.util.List;

/**
 * 作者：Koterwong，创建日期：2016/4/24.
 * Description:
 */
public interface WeatherView {

    void showToastMsg(String msg);

    void showLoadingVisible();

    void showSuccessVisible();

    void showErrorVisible();

    void setSwipeRefreshLayoutStatue(boolean statue);

    boolean isSwipeRefreshLayoutRefreshing();

    void setWeatherIcon(int ResId);

    void setWeatherDes(String mWeatherDes);

    void setWeatherTmp(String mWeatherTmp);

    void setWeatherUpdateTime(String time);
    void setWeatherPm25(String time);

    void refreshDailyView(List<WeatherBean.DailyForecastBean> mDailyForecastList);

    void refreshSuggestView(WeatherBean.SuggestionBean suggestionBean);

    void refreshOverview(WeatherBean.NowBean nowBean);
}
