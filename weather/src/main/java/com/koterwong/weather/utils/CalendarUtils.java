package com.koterwong.weather.utils;

import java.util.Calendar;

/**
 * ================================================
 * Created By：Koterwong; Time: 2016/06/10 08:34
 * <p>
 * Description:
 * =================================================
 */
public class CalendarUtils {

    public static boolean isDay(){
        Calendar calendar = Calendar.getInstance();
        int time = calendar.get(Calendar.HOUR_OF_DAY);
        return time > 6 && time < 18;
    }
}
