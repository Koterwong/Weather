package com.koterwong.weather.ui.choicecity.Model;

/**
 * Author：Koterwong，Data：2016/4/25.
 * Description:
 */
public interface CityModel {

//    void copyDatabase(CityModelImp.LoadCityDBListener listener);

    void queryProvince(CityModelImp.QueryProListener listener);

    void queryCity(String proId,CityModelImp.QueryCityListener listener);

}
