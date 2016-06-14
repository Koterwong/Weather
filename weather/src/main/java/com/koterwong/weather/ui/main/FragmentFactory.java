package com.koterwong.weather.ui.main;

import android.support.v4.app.Fragment;

import com.koterwong.weather.ui.weather.WeatherLazyFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * ================================================
 * Created By：Koterwong; Time: 2016/06/14 13:12
 * <p/>
 * Description:
 * =================================================
 */
public class FragmentFactory {

    private static Map<String, Fragment> mFragments = new HashMap<>();

    public static Fragment createFragment(String city) {
        Fragment fragment = mFragments.get(city);
        if (fragment == null) {
            fragment = WeatherLazyFragment.newInstance(city);
        }
        return fragment;
    }
}
