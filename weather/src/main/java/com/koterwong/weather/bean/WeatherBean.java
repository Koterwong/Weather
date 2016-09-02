package com.koterwong.weather.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Author：Koterwong，Data：2016/4/24.
 * Description:
 */
public class WeatherBean implements Serializable{


    /**
     * city : {"aqi":"86","pm10":"109","pm25":"63"}
     */
    public AqiBean aqi;

    /**
     * city : 淄博
     * cnty : 中国
     * id : CN101120301
     * lat : 36.788000
     * lon : 118.050000
     * update : {"loc":"2016-04-24 15:49","utc":"2016-04-24 07:49"}
     */
    public BasicBean basic;

    /**
     * cond : {"code":"100","txt":"晴"}
     * fl : 27
     * hum : 32
     * pcpn : 0
     * pres : 1012
     * tmp : 26
     * vis : 10
     * wind : {"deg":"233","dir":"南风","sc":"5-6","spd":"30"}
     */
    public NowBean now;

    /**
     * aqi : {"city":{"aqi":"86","pm10":"109","pm25":"63"}}
     * basic : {"city":"淄博","cnty":"中国","id":"CN101120301","lat":"36.788000","lon":"118.050000","update":{"loc":"2016-04-24 15:49","utc":"2016-04-24 07:49"}}
     * daily_forecast : [{"astro":{"mr":"20:39","ms":"06:41","sr":"05:21","ss":"18:50"},"cond":{"code_d":"100","code_n":"100","txt_d":"晴","txt_n":"晴"},"date":"2016-04-24","hum":"24","pcpn":"0.0","pop":"0","pres":"1012","tmp":{"max":"27","min":"15"},"vis":"10","wind":{"deg":"232","dir":"南风","sc":"3-4","spd":"12"}},{"astro":{"mr":"21:33","ms":"07:20","sr":"05:20","ss":"18:51"},"cond":{"code_d":"100","code_n":"100","txt_d":"晴","txt_n":"晴"},"date":"2016-04-25","hum":"15","pcpn":"0.0","pop":"0","pres":"1015","tmp":{"max":"28","min":"13"},"vis":"10","wind":{"deg":"163","dir":"南风","sc":"微风","spd":"0"}},{"astro":{"mr":"22:25","ms":"08:03","sr":"05:19","ss":"18:51"},"cond":{"code_d":"101","code_n":"101","txt_d":"多云","txt_n":"多云"},"date":"2016-04-26","hum":"19","pcpn":"0.0","pop":"0","pres":"1013","tmp":{"max":"25","min":"12"},"vis":"10","wind":{"deg":"123","dir":"北风","sc":"微风","spd":"3"}},{"astro":{"mr":"23:15","ms":"08:50","sr":"05:17","ss":"18:52"},"cond":{"code_d":"101","code_n":"101","txt_d":"多云","txt_n":"多云"},"date":"2016-04-27","hum":"29","pcpn":"0.0","pop":"0","pres":"1011","tmp":{"max":"22","min":"11"},"vis":"10","wind":{"deg":"39","dir":"东北风","sc":"微风","spd":"0"}},{"astro":{"mr":"null","ms":"09:41","sr":"05:16","ss":"18:53"},"cond":{"code_d":"101","code_n":"100","txt_d":"多云","txt_n":"晴"},"date":"2016-04-28","hum":"21","pcpn":"0.0","pop":"0","pres":"1014","tmp":{"max":"23","min":"12"},"vis":"10","wind":{"deg":"3","dir":"南风","sc":"微风","spd":"6"}},{"astro":{"mr":"00:02","ms":"10:36","sr":"05:15","ss":"18:54"},"cond":{"code_d":"100","code_n":"100","txt_d":"晴","txt_n":"晴"},"date":"2016-04-29","hum":"20","pcpn":"0.0","pop":"1","pres":"1009","tmp":{"max":"29","min":"18"},"vis":"10","wind":{"deg":"233","dir":"南风","sc":"微风","spd":"0"}},{"astro":{"mr":"00:47","ms":"11:36","sr":"05:14","ss":"18:55"},"cond":{"code_d":"101","code_n":"100","txt_d":"多云","txt_n":"晴"},"date":"2016-04-30","hum":"26","pcpn":"0.0","pop":"0","pres":"1002","tmp":{"max":"29","min":"10"},"vis":"9","wind":{"deg":"222","dir":"南风","sc":"4-5","spd":"22"}}]
     * hourly_forecast : [{"date":"2016-04-24 16:00","hum":"25","pop":"0","pres":"1011","tmp":"27","wind":{"deg":"228","dir":"西南风","sc":"3-4","spd":"24"}},{"date":"2016-04-24 19:00","hum":"32","pop":"0","pres":"1012","tmp":"25","wind":{"deg":"218","dir":"西南风","sc":"3-4","spd":"19"}},{"date":"2016-04-24 22:00","hum":"40","pop":"0","pres":"1014","tmp":"23","wind":{"deg":"206","dir":"西南风","sc":"3-4","spd":"19"}}]
     * now : {"cond":{"code":"100","txt":"晴"},"fl":"27","hum":"32","pcpn":"0","pres":"1012","tmp":"26","vis":"10","wind":{"deg":"233","dir":"南风","sc":"5-6","spd":"30"}}
     * status : ok
     * suggestion : {"comf":{"brf":"较舒适","txt":"白天天气晴好，您在这种天气条件下，会感觉早晚凉爽、舒适，午后偏热。"},"cw":{"brf":"较适宜","txt":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"},"drsg":{"brf":"舒适","txt":"建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。"},"flu":{"brf":"较易发","txt":"昼夜温差较大，较易发生感冒，请适当增减衣服。体质较弱的朋友请注意防护。"},"sport":{"brf":"较适宜","txt":"天气较好，但风力较大，推荐您进行室内运动，若在户外运动请注意避风保暖。"},"trav":{"brf":"适宜","txt":"天气较好，风稍大，但温度适宜，是个好天气哦。适宜旅游，您可以尽情地享受大自然的无限风光。"},"uv":{"brf":"强","txt":"紫外线辐射强，建议涂擦SPF20左右、PA++的防晒护肤品。避免在10点至14点暴露于日光下。"}}
     */
    public String status;

    /**
     * comf : 舒适指数{"brf":"较舒适","txt":"白天天气晴好，您在这种天气条件下，会感觉早晚凉爽、舒适，午后偏热。"}
     * cw :  洗车指数 {"brf":"较适宜","txt":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"}
     * drsg :穿衣指数 {"brf":"舒适","txt":"建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。"}
     * flu : 感冒指数{"brf":"较易发","txt":"昼夜温差较大，较易发生感冒，请适当增减衣服。体质较弱的朋友请注意防护。"}
     * sport :运动指数  {"brf":"较适宜","txt":"天气较好，但风力较大，推荐您进行室内运动，若在户外运动请注意避风保暖。"}
     * trav : 旅游指数{"brf":"适宜","txt":"天气较好，风稍大，但温度适宜，是个好天气哦。适宜旅游，您可以尽情地享受大自然的无限风光。"}
     * uv :  紫外线指数{"brf":"强","txt":"紫外线辐射强，建议涂擦SPF20左右、PA++的防晒护肤品。避免在10点至14点暴露于日光下。"}
     */
    public SuggestionBean suggestion;

    /**
     * astro : {"mr":"20:39","ms":"06:41","sr":"05:21","ss":"18:50"}
     * cond : {"code_d":"100","code_n":"100","txt_d":"晴","txt_n":"晴"}
     * date : 2016-04-24
     * hum : 24
     * pcpn : 0.0
     * pop : 0
     * pres : 1012
     * tmp : {"max":"27","min":"15"}
     * vis : 10
     * wind : {"deg":"232","dir":"南风","sc":"3-4","spd":"12"}
     */
    @SerializedName("daily_forecast")
    public List<DailyForecastBean> dailyForecastList;

    /**
     * date : 2016-04-24 16:00
     * hum : 25
     * pop : 0
     * pres : 1011
     * tmp : 27
     * wind : {"deg":"228","dir":"西南风","sc":"3-4","spd":"24"}
     */
    @SerializedName("hourly_forecast")
    public List<HourlyForecastBean> hourlyForecastList;

    public static class AqiBean implements Serializable{

        /**
         * aqi : 86
         * pm10 : 109
         * pm25 : 63
         */
        public CityBean city;

        public static class CityBean implements Serializable{
            public String aqi;
            public String pm10;
            public String pm25;
        }
    }

    public static class BasicBean implements Serializable{
        public String city;
        public String lat;
        public String lon;
        /**
         * loc : 2016-04-24 15:49
         * utc : 2016-04-24 07:49
         */

        public UpdateBean update;

        public static class UpdateBean implements Serializable{
            public String loc;
        }
    }

    public static class NowBean implements Serializable{

        public String hum;
        public String pcpn;
        public String pres;
        public String tmp;
        public String vis;

        /**
         * code : 100
         * txt : 晴
         */
        public CondBean cond;

        /**
         * deg : 233
         * dir : 南风
         * sc : 5-6
         * spd : 30
         */
        public WindBean wind;

        public static class CondBean implements Serializable {
            public String code;
            public String txt;
        }

        public static class WindBean implements Serializable{
            public String dir;
            public String sc;
            public String spd;
        }
    }

    public static class SuggestionBean implements Serializable{

        /**
         * brf : 较舒适
         * txt : 白天天气晴好，您在这种天气条件下，会感觉早晚凉爽、舒适，午后偏热。
         */
        public ComfBean comf;

        /**
         * brf : 较适宜
         * txt : 较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。
         */
        public CwBean cw;
        /**
         * brf : 舒适
         * txt : 建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。
         */

        public DrsgBean drsg;

        /**
         * brf : 较易发
         * txt : 昼夜温差较大，较易发生感冒，请适当增减衣服。体质较弱的朋友请注意防护。
         */
        public FluBean flu;
        /**
         * brf : 较适宜
         * txt : 天气较好，但风力较大，推荐您进行室内运动，若在户外运动请注意避风保暖。
         */

        public SportBean sport;

        /**
         * brf : 适宜
         * txt : 天气较好，风稍大，但温度适宜，是个好天气哦。适宜旅游，您可以尽情地享受大自然的无限风光。
         */
        public TravBean trav;

        /**
         * brf : 强
         * txt : 紫外线辐射强，建议涂擦SPF20左右、PA++的防晒护肤品。避免在10点至14点暴露于日光下。
         */
        public UvBean uv;

        public static class ComfBean implements Serializable{
            public String brf;
            public String txt;
        }

        public static class CwBean implements Serializable{
            public String brf;
            public String txt;
        }

        public static class DrsgBean implements Serializable{
            public String brf;
            public String txt;
        }

        public static class FluBean implements Serializable{
            public String brf;
            public String txt;
        }

        public static class SportBean implements Serializable{
            public String brf;
            public String txt;
        }

        public static class TravBean implements Serializable{
            public String brf;
            public String txt;
        }

        public static class UvBean implements Serializable{
            public String brf;
            public String txt;
        }
    }

    public static class DailyForecastBean implements Serializable{
        /**
         * mr : 20:39
         * ms : 06:41
         * sr : 05:21
         * ss : 18:50
         */

        public AstroBean astro;
        /**
         * code_d : 100
         * code_n : 100
         * txt_d : 晴
         * txt_n : 晴
         */

        public CondBean cond;
        public String date;
        public String hum;
        public String pcpn;
        public String pop;
        public String pres;
        /**
         * max : 27
         * min : 15
         */

        public TmpBean tmp;
        public String vis;
        /**
         * deg : 232
         * dir : 南风
         * sc : 3-4
         * spd : 12
         */

        public WindBean wind;

        public static class AstroBean implements Serializable{
            public String mr;
            public String ms;
            public String sr;
            public String ss;
        }

        public static class CondBean implements Serializable{
            public String code_d;
            public String code_n;
            public String txt_d;
            public String txt_n;
        }

        public static class TmpBean implements Serializable{
            public String max;
            public String min;
        }

        public static class WindBean implements Serializable{
            public String dir;
            public String sc;
            public String spd;
        }
    }

    public static class HourlyForecastBean implements Serializable{
        public String date;
        public String hum;
        public String pop;
        public String pres;
        public String tmp;
        /**
         * deg : 228
         * dir : 西南风
         * sc : 3-4
         * spd : 24
         */

        public WindBean wind;

        public static class WindBean implements Serializable{
            public String dir;
            public String sc;
            public String spd;
        }
    }
}
