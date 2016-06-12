package com.koterwong.weather.ui.weather.model;

import com.koterwong.weather.MyApp;
import com.koterwong.weather.beans.WeatherBean;
import com.koterwong.weather.commons.UrlHelper;
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
//    private CompositeSubscription mCompositeSubscription;
//
//    public WeatherModelImp() {
//        mCompositeSubscription = new CompositeSubscription();
//    }

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
                        if (listener != null) {
                            if (weatherBean != null) {
                                listener.onLoadServiceSuccess(weatherBean);
                            } else {
                                listener.onLoadServiceFailed(new Exception("暂时没有该城市天气数据"));
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
            return WeatherJsonUtil.parseJSON(cityName, json);
        }
    }

    @Override public void loadLocation(final String cityName, final LoadLocationListener listener) {
//        //RxJava替换线程,有数据显示的bug，能力限制、展示待不做替换。
//        Subscription subscribe = Observable.create(new Observable.OnSubscribe<WeatherBean>() {
//            @Override public void call(Subscriber<? super WeatherBean> subscriber) {
//                subscriber.onNext(WeatherJsonUtil.getLocWeatherBean(cityName));
//            }
//        }).subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(Schedulers.io())
//                .subscribe(new Subscriber<WeatherBean>() {
//                    @Override public void onCompleted() {
//
//                    }
//
//                    @Override public void onError(Throwable e) {
//                        listener.onLoadLocFailed(new Exception("本地没有缓存"));
//                    }
//
//                    @Override public void onNext(WeatherBean weatherBean) {
//                        if (weatherBean != null) {
//                            listener.onLoadLocSuccess(weatherBean);
//                        } else {
//                            listener.onLoadLocFailed(new Exception("本地没有缓存"));
//                        }
//                    }
//                });
//        mCompositeSubscription.add(subscribe);
        new Thread(new Runnable() {
            @Override public void run() {
                final WeatherBean weatherBean = WeatherJsonUtil.getLocWeatherBean(cityName);
                MyApp.getHandler().post(new Runnable() {
                    @Override public void run() {
                        if (weatherBean != null) {
                            listener.onLoadLocSuccess(weatherBean);
                        } else {
                            listener.onLoadLocFailed(new Exception("本地没有缓存"));
                        }
                    }
                });
            }
        }).start();
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
