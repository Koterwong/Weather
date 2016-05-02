package com.koterwong.weather.weather;

import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koterwong.weather.base.BaseApplication;
import com.koterwong.weather.beans.WeatherBean;
import com.koterwong.weather.R;
import com.koterwong.weather.utils.ACache;
import com.koterwong.weather.utils.JsonUtils;
import com.koterwong.weather.utils.L;

/**
 * Author：Koterwong，Data：2016/4/24.
 * Description:
 */
public class WeatherJsonUtil {

    /**
     * 解析JSON数据
     *
     * @param json
     * @return 失败返回null
     */
    public static WeatherBean parseJSON(String cityName,String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        L.d(json);
        try {
            /**
             * 解析天气数据。
             */
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(json).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("HeWeather data service 3.0");
            JsonElement jsonElement = jsonArray.get(0);
            JsonObject jsonObject1 = jsonElement.getAsJsonObject();
            if ("ok".equals(jsonObject1.get("status").getAsString())) {
                /*数据请求正确*/
                WeatherBean weatherBean = JsonUtils.deserialize(jsonObject1, WeatherBean.class);
                /*将WeatherBean保存到本地，设置过期时间为8小时*/
                BaseApplication.getACache().put(cityName, weatherBean, ACache.TIME_HOUR * 8);
                return weatherBean;
            } else {
                //数据请求失败
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 从本地读取WeatherBean
     *
     * @return success weather，failed null
     */
    public static WeatherBean getLocWeatherBean(String city) {
        return (WeatherBean) BaseApplication.getACache().getAsObject(city);
    }

    public static int getWeatherImage(String weather) {
        if ("晴".equals(weather)) {
            return R.mipmap.weather_sundy;
        } else if ("多云".equals(weather) || "少云".equals(weather) || "晴间多云".equals(weather) || "阴".equals(weather)) {
            return R.mipmap.weather_clound;
        } else if (weather.contains("风")) {
            return R.mipmap.weather_wind;
        } else if ("雷阵雨".equals(weather) || "强雷阵雨".equals(weather)) {
            return R.mipmap.weather_thunder_rain;
        } else if (weather.contains("阵雨")) {
            return R.mipmap.weather_zhenyu;
        } else if ("小雨".equals(weather) || "毛毛雨/细雨".equals(weather)) {
            return R.mipmap.weather_light_rain;
        } else if ("中雨".equals(weather)) {
            return R.mipmap.weather_rain;
        } else if ("大雨".equals(weather) || "暴雨".equals(weather) || "大暴雨".equals(weather) || "特大暴雨".equals(weather)) {
            return R.mipmap.weather_heavy_rain;
        } else if ("小雪".equals(weather)) {
            return R.mipmap.weather_light_sonw;
        } else if ("中雪".equals(weather)) {
            return R.mipmap.weather_sonw;
        } else if ("大雪".equals(weather) || "暴雪".equals(weather)) {
            return R.mipmap.weather_havery_snow;
        } else if ("雨雪天气".equals(weather) || "阵雨夹雪".equals(weather) || "雨夹雪".equals(weather)) {
            return R.mipmap.weather_sonw_rain;
        } else if ("阵雪".equals(weather)) {
            return R.mipmap.weather_zhenxue;
        } else if (weather.contains("雾") || weather.contains("霾")) {
            return R.mipmap.weather_fog;
        }
        return R.mipmap.weather12;
    }
}