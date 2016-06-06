package com.koterwong.weather.weather.model;

import android.os.Process;

import com.koterwong.weather.BaseApplication;
import com.koterwong.weather.beans.WeatherBean;
import com.koterwong.weather.commons.UrlHelper;
import com.koterwong.weather.utils.ThreadManager;
import com.koterwong.weather.weather.WeatherJsonUtil;
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
    @Override
    public void loadWeatherFromServer(String cityName, final LoadServiceListener listener) {
        this.cityName = cityName;
        /**
         * 替换不必要的字符串。
         */
        cityName = cityName
                .replace("市", "")
                .replace("省", "")
                .replace("自治区", "")
                .replace("特别行政区", "")
                .replace("地区", "")
                .replace("盟", "")
                .replace("县", "");
        String mUrl = UrlHelper.url + cityName;
        OkHttpUtils.get()
                .url(mUrl)
                .addHeader(UrlHelper.key, UrlHelper.apiKeyValue)
                .build()
                .connTimeOut(8000)
                .readTimeOut(8000)
                .execute(new WeatherCallBack() {
                    /**
                     * 请求失败
                     * @param call
                     * @param e   错误信息
                     */
                    @Override
                    public void onError(Call call, Exception e) {
                        if (listener != null) {
                            listener.onLoadServiceFailed(e);
                        }
                    }

                    /**
                     * 请求成功，并且将数据解析
                     * @param weatherBean 解析结果
                     */
                    @Override
                    public void onResponse(WeatherBean weatherBean) {
                        if (listener!=null){
                            if (weatherBean != null) {
                            /*数据解析成功*/
                                listener.onLoadServiceSuccess(weatherBean);
                            } else {
                            /* 数据解析失败 ，校园网不不能访问API接口数据 */
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

        /**
         * 运行在子线程，将解析Json的操作放到子线程中执行。
         *
         * @param response
         * @return 将解析成功的Weather对象，通过回调返回onResponse。在主线程运行
         * @throws IOException
         */
        @Override
        public WeatherBean parseNetworkResponse(Response response) throws IOException {
            String json = response.body().string();
            return WeatherJsonUtil.parseJSON(cityName,json);
        }
    }

    /**
     * 加载本地数据
     *
     * @param listener
     */
    @Override
    public void loadLocation(final String cityName, final LoadLocationListener listener) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                final WeatherBean weatherBean = WeatherJsonUtil.getLocWeatherBean(cityName);
                /**
                 * 将跟新UI的操作放到主线程主执行
                 */
                if (Process.myTid()==BaseApplication.getMainId()){
                    if (weatherBean != null) {
                        listener.onLoadLocSuccess(weatherBean);
                    } else {
                        listener.onLoadLocFailed(new Exception("本地没有缓存~"));
                    }
                }else {
                    BaseApplication.getHandler().post( new Runnable() {
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
