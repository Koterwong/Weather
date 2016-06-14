package com.koterwong.weather.ui.weather.presenter;

import com.koterwong.weather.MyApp;
import com.koterwong.weather.beans.WeatherBean;
import com.koterwong.weather.ui.weather.WeatherJsonUtil;
import com.koterwong.weather.ui.weather.model.WeatherModel;
import com.koterwong.weather.ui.weather.model.WeatherModelImp;
import com.koterwong.weather.ui.weather.view.WeatherView;
import com.koterwong.weather.utils.ToolsUtil;

/**
 * 作者：Koterwong，创建日期：2016/4/24.
 * Description:
 */
public class WeatherPresenterImp implements
        WeatherPresenter,
        WeatherModelImp.LoadLocationListener,
        WeatherModelImp.LoadServiceListener {

    private String mCity;

    private WeatherView mWeatherView;
    private WeatherModel mWeatherModel;

    public WeatherPresenterImp(WeatherView mMainView) {
        this.mWeatherView = mMainView;
        mWeatherModel = new WeatherModelImp();
    }

    public void loadWeatherData(String city) {
        if (city == null) {
            mWeatherView.showToastMsg("未接受到城市数据");
            return;
        }
        this.mCity = city;
        if (!ToolsUtil.isNetworkAvailable(MyApp.getApp())){
            mWeatherView.showToastMsg("网络为连接");
            mWeatherView.setSwipeRefreshLayoutStatue(false);
            return;
        }
        //改为加载网路数据,if（失败） loadlocal
        mWeatherView.showLoadingVisible();
        mWeatherModel.loadWeatherFromServer(city,this);
    }

    /**
     * 加载服务器成功的回调
     */
    @Override public void onLoadServiceSuccess(WeatherBean weatherBean) {
        upDateWeatherUI(weatherBean);
    }

    /**
     * 加载服务器失败的回调
     */
    @Override public void onLoadServiceFailed(Exception e) {
        //加载失败原因：1.请求超时，2.没有该城市天气信息。
        mWeatherView.showToastMsg(e.getMessage());
        mWeatherModel.loadLocation(mCity,this);
    }

    /**
     * 加载本地成功的回调。
     */
    @Override public void onLoadLocSuccess(WeatherBean weatherBean) {
        upDateWeatherUI(weatherBean);
    }

    /**
     * 加载本地失败的回调。
     */
    @Override public void onLoadLocFailed(Exception e) {
        if (mWeatherView.isSwipeRefreshLayoutRefreshing()){
            mWeatherView.setSwipeRefreshLayoutStatue(false);
        }
        this.showErrorPage();
    }

    private void showErrorPage(){
        mWeatherView.showErrorVisible();
    }

    private void upDateWeatherUI(WeatherBean weatherBean) {
        //显示加载成功界面
        mWeatherView.showSuccessVisible();
        //如果为下拉刷新状态，返回正常状态
        if (mWeatherView.isSwipeRefreshLayoutRefreshing())
            mWeatherView.setSwipeRefreshLayoutStatue(false);
        mWeatherView.setWeatherIcon(WeatherJsonUtil.getWeatherImgResType2(weatherBean.now.cond.txt));
        mWeatherView.setWeatherDes(weatherBean.now.cond.txt);
        mWeatherView.setWeatherTmp(weatherBean.now.tmp + "°");
        mWeatherView.setWeatherUpdateTime(weatherBean.basic.update.loc.substring(11));
        if (weatherBean.aqi != null)
            mWeatherView.setWeatherPm25(String.format("pm25:%s",weatherBean.aqi.city.pm25));
        //刷新未来五天的天气
        mWeatherView.refreshDailyView(weatherBean.dailyForecastList);
        //刷新今日天气其他数据OverView
        mWeatherView.refreshOverview(weatherBean.now);
        //刷新生活建议页面
        mWeatherView.refreshSuggestView(weatherBean.suggestion);
    }

    private String getAqi(String aqi) {
        int aqiNumber = Integer.parseInt(aqi);
        if (aqiNumber <= 50) {
            return "空气质量优";
        } else if (aqiNumber > 50 && aqiNumber <= 100) {
            return "空气质量良好";
        } else if (aqiNumber > 100 && aqiNumber <= 150) {
            return "空气轻度污染";
        } else if (aqiNumber > 150 && aqiNumber <= 200) {
            return "空气中度污染";
        } else if (aqiNumber > 200 && aqiNumber <= 300) {
            return "空气重度污染";
        } else if (aqiNumber > 300) {
            return "空气严重污染";
        }
        return "空气质量未知";
    }
}
