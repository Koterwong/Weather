package com.koterwong.weather.weather.presenter;

import com.koterwong.weather.base.BaseApplication;
import com.koterwong.weather.beans.WeatherBean;
import com.koterwong.weather.utils.ToolsUtil;
import com.koterwong.weather.weather.WeatherJsonUtil;
import com.koterwong.weather.weather.model.WeatherModel;
import com.koterwong.weather.weather.model.WeatherModelImp;
import com.koterwong.weather.weather.view.WeatherView;

import java.util.List;

/**
 * 作者：Koterwong，创建日期：2016/4/24.
 * Description:
 */
public class WeatherPresenterImp implements WeatherPresenter, WeatherModelImp.LoadLocationListener, WeatherModelImp.LoadServiceListener {

    private String mCity;
    /**
     * mDatas
     */
    private WeatherBean.NowBean mNowBean;                       //当前天气
    private List<WeatherBean.DailyForecastBean> mDailyList;    //七天天气预报
    private WeatherBean.AqiBean mApiBean;                       //空气质量指数
    private WeatherBean.BasicBean.UpdateBean mUpdataBean;       //发布时间

    private WeatherView mWeatherView;
    private WeatherModel mWeatherModel;

    public WeatherPresenterImp(WeatherView mMainView) {
        this.mWeatherView = mMainView;
        mWeatherModel = new WeatherModelImp();
    }


    /**
     * 获取天气数据。
     *
     * @param city 具体城市
     */
    public void loadWeatherData(String city) {
        if (city == null) {
            mWeatherView.showToastMsg("未接受到城市数据~");
            return;
        }
        mCity = city;

        if (mWeatherView.isSwipeRefreshLayoutRefreshing()) {
            /**
             * 如果为下拉刷新状态，才去去服务加载数据
             */
            if (!ToolsUtil.isNetworkAvailable(BaseApplication.getApplication())) {
                mWeatherView.showToastMsg("网络未连接~");
                return;
            }
            mWeatherModel.loadWeatherFromServer(city, this);
        } else {
            /**
             * 不是下拉刷新状态，则直接加载本地数据
             */
            mWeatherView.showLoadingVisible();
            mWeatherModel.loadLocation(city,this);
        }
    }

    /**
     * 加载服务器成功的回调
     *
     * @param weatherBean
     */
    @Override
    public void onLoadServiceSuccess(WeatherBean weatherBean) {
        mNowBean = weatherBean.now;
        mApiBean = weatherBean.aqi;
        mDailyList = weatherBean.dailyForecastList;
        mUpdataBean = weatherBean.basic.update;
        upDateWeatherUI();
    }

    /**
     * 加载服务器失败的回调
     *
     * @param e
     */
    @Override
    public void onLoadServiceFailed(Exception e) {
        /*加载失败原因：1.请求超时，2.没有该城市天气信息。*/
        mWeatherView.showToastMsg(e.toString());
        if (mWeatherView.isSwipeRefreshLayoutRefreshing()){
            //下拉刷新失败
            mWeatherView.setmSwipeRefreshLayoutStatue(false);
        }else{
            mWeatherView.showErrorVisible();
        }
    }

    /**
     * 加载本地成功的回调。
     *
     * @param weatherBean
     */
    @Override
    public void onLoadLocSuccess(WeatherBean weatherBean) {
        mNowBean = weatherBean.now;
        mApiBean = weatherBean.aqi;
        mDailyList = weatherBean.dailyForecastList;
        mUpdataBean = weatherBean.basic.update;
        upDateWeatherUI();
    }

    /**
     * 加载本地失败的回调。
     *
     * @param e
     */
    @Override
    public void onLoadLocFailed(Exception e) {
        if (mCity != null)
            mWeatherModel.loadWeatherFromServer(mCity, this);
    }

    /*更新UI*/
    private void upDateWeatherUI() {
        //显示加载成功界面
        mWeatherView.showSuccessVisible();
        //如果为下拉刷新状态，返回正常状态
        if (mWeatherView.isSwipeRefreshLayoutRefreshing())
            mWeatherView.setmSwipeRefreshLayoutStatue(false);

        mWeatherView.setmWeatherIcon(WeatherJsonUtil.getWeatherImage(mNowBean.cond.txt));
        mWeatherView.setmWeatherDes(mNowBean.cond.txt);
        mWeatherView.setmWeatherTmp(mNowBean.tmp);
        if (mApiBean != null) {
            mWeatherView.setmWeatherAqi(mApiBean.city.aqi + getAqi(mApiBean.city.aqi));
        } else {
            mWeatherView.setmWeatherAqi("没有该城市空气质量指数");
        }
        mWeatherView.setmWeatherHum("湿度：" + mNowBean.hum+"%");
        mWeatherView.setmWeatherWind(mNowBean.wind.dir + mNowBean.wind.sc + "级");
        String time = mUpdataBean.loc.substring(11);
        mWeatherView.setmWeatherUpdateTime(time + "更新");
        //刷新未来五天的天气
        refreshWeatherDaily();
        //刷新生活建议界面
        refreshWeatherSug();
    }

    private void refreshWeatherDaily() {
        mWeatherView.refreshWeatherDaily(mDailyList);
    }

    private void refreshWeatherSug() {

    }

    /*获取空气质量指数*/
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
