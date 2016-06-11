package com.koterwong.weather.ui.weather.model;

import android.os.Process;

import com.koterwong.weather.MyApp;
import com.koterwong.weather.beans.WeatherBean;
import com.koterwong.weather.commons.UrlHelper;
import com.koterwong.weather.utils.ThreadManager;
import com.koterwong.weather.ui.weather.WeatherJsonUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者：Koterwong，创建日期：2016/4/24.
 * <p/>
 * Description:
 */
public class WeatherModelImp implements WeatherModel {


    private String cityName;
    /**
     * 加载网络数据
     *
     * @param cityName 城市名称
     * @param listener 加载完成的监听
     */
    @Override public void loadWeatherFromServer(String cityName, final LoadServiceListener listener) {
        this.cityName = cityName;
        cityName = cityName
                .replace("市", "")
                .replace("省", "")
                .replace("自治区", "")
                .replace("特别行政区", "")
                .replace("地区", "")
                .replace("盟", "")
                .replace("县", "");
        String mUrl = UrlHelper.BASE_URL + cityName;
        OkHttpUtils.get()
                .url(mUrl)
                .addHeader(UrlHelper.key, UrlHelper.apiKeyValue)
                .build()
                .connTimeOut(8000)
                .readTimeOut(8000)
                .execute(new WeatherCallBack() {
                    @Override public void onError(Call call, Exception e) {
                        if (listener != null) {
                            listener.onLoadServiceFailed(e);
                        }
                    }
                    @Override public void onResponse(WeatherBean weatherBean) {
                        if (listener!=null){
                            if (weatherBean != null) {
                                listener.onLoadServiceSuccess(weatherBean);
                            } else {
                                listener.onLoadServiceFailed(new Exception("没有该城市数据，sorry~"));
                            }
                        }
                    }
                });

    }

    /**
     * 自定义callBack将解析数据的操作放到子线程去执行。
     */
    public abstract class WeatherCallBack extends Callback<WeatherBean> {

        @Override public WeatherBean parseNetworkResponse(Response response) throws IOException {
            String json = response.body().string();
            return WeatherJsonUtil.parseJSON(cityName,json);
        }
    }

    @Override public void loadLocation(final String cityName, final LoadLocationListener listener) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                final WeatherBean weatherBean = WeatherJsonUtil.getLocWeatherBean(cityName);
                //将跟新UI的操作放到主线程主执行
                if (Process.myTid()== MyApp.getMainId()){
                    if (weatherBean != null) {
                        listener.onLoadLocSuccess(weatherBean);
                    } else {
                        listener.onLoadLocFailed(new Exception("本地没有缓存~"));
                    }
                }else {
                    MyApp.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            if (weatherBean != null) {
                                listener.onLoadLocSuccess(weatherBean);
                            } else {
                                listener.onLoadLocFailed(new Exception("本地没有缓存~"));
                            }
                        }
                    });
                }
            }
        });
    }

    public interface LoadServiceListener {
        void onLoadServiceSuccess(WeatherBean weatherBean);

        void onLoadServiceFailed(Exception e);
    }

    public interface LoadLocationListener {
        void onLoadLocSuccess(WeatherBean weatherBean);

        void onLoadLocFailed(Exception e);
    }
}
