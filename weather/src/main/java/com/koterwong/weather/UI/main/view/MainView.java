package com.koterwong.weather.ui.main.view;

import java.util.List;

/**
 * Author：Koterwong，Data：2016/4/27.
 * Description:
 */
public interface MainView {

//    void switch2ChoiceCityActivity();
//
//    void switch2ManagerCityActivity();
//
//    void switch2SettingActivity();
//
//    void switch2AboutActivity();

    void switch2Activity(Class activityClass);

    void switch2ActivityForResult(Class activityClass,int reqCode);

    void setCities(List<String> mCityList);

    void addCity(String cityName);

    void setToolbarTitle(String title);

    void setContentVisible(boolean visible);

}
