package com.koterwong.weather.main.presenter;

/**
 * Author：Koterwong，Data：2016/4/27.
 * Description:
 */
public interface MainPresenter {
    void switchNavigation(int position);

    void loadCities();
    void addCity(String cityName);
    void deleteCity(String cityName);

}
