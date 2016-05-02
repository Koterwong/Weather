package com.koterwong.weather.choicecity.View;

import com.koterwong.weather.beans.City;
import com.koterwong.weather.beans.Province;

import java.util.List;

/**
 * Author：Koterwong，Data：2016/4/25.
 * Description:
 */
public interface CityView {

    void setProDatas(List<Province> mDatas);
    void setCityDatas(List<City> mDatas);

    void showProgressBar();

    void hideProgressBar();

    void showToast(String toast);

    void setTitle(String title);

}
