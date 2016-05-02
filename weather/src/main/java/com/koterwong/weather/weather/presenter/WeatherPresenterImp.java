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
public class WeatherPresenterImp implements WeatherPresenter, WeatherModelImp.LoadLocationListener {

    private static final String TAG = "WeatherPresenterImp";

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
    public void loadWeatherData(final String city) {
        //加载中，将界面置为progressBar。如果为下拉刷新状态，不去显示progress
        if (!mWeatherView.isSwipeRefreshLayoutRefreshing())
            mWeatherView.setContentVisible(false);
        if (city == null) {
            mWeatherView.showToastMsg("未接受到城市数据~");
            return;
        }
        if (!ToolsUtil.isNetworkAvailable(BaseApplication.getApplication())) {
            //网络不可用,加载本地数据
            mWeatherView.showToastMsg("网络未连接~");
            mWeatherModel.loadLocation(city,this);
            return;
        }
        mWeatherModel.loadWeatherFromServer(city, new WeatherModelImp.LoadWeatherListener() {

            @Override
            public void onLoadSuccess(WeatherBean weatherBean) {
                mNowBean = weatherBean.now;
                mApiBean = weatherBean.aqi;
                mDailyList = weatherBean.dailyForecastList;
                mUpdataBean = weatherBean.basic.update;
                upDateWeatherUI();
            }

            @Override
            public void onLoadFailed(Exception e) {
                /*加载失败原因：1.请求超时，2.没有该城市天气信息。*/
                mWeatherView.showToastMsg(e.toString());
                //加载本地数据
                mWeatherModel.loadLocation(city,WeatherPresenterImp.this);
            }

        });
    }

    /*更新UI*/
    private void upDateWeatherUI() {
        //加载成功将界面置为显示
        mWeatherView.setContentVisible(true);
        //如果，为下拉刷新状态，返回正常状态
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
        mWeatherView.setmWeatherHum("湿度：" + mNowBean.hum);
        mWeatherView.setmWeatherWind(mNowBean.wind.dir + mNowBean.wind.sc + "级");
        String time = mUpdataBean.loc.substring(11);
        mWeatherView.setmWeatherUpdateTime(time + "更新");
        //刷新未来五天的天气
        refreshWeatherDaily();
        //刷新生活建议界面
    }

    private void refreshWeatherDaily() {
        mWeatherView.refreshWeatherDaily(mDailyList);
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
        mWeatherView.showToastMsg("加载本地数据成功~");
    }

    /**
     * 加载本地失败的回调。
     *
     * @param e
     */
    @Override
    public void onLoadLocFailed(Exception e) {
        /*网络和本地都加载失败，同样将界面置为显示状态*/
        mWeatherView.setContentVisible(true);
        mWeatherView.setLoadEmpty();
        //如果，为下拉刷新状态，返回正常状态
        if (mWeatherView.isSwipeRefreshLayoutRefreshing())
            mWeatherView.setmSwipeRefreshLayoutStatue(false);
//        mWeatherView.showToastMsg(e.toString());
    }
}
