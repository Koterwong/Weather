package com.koterwong.weather.ui.choicecity.View;

import com.koterwong.weather.bean.CityBean;
import com.koterwong.weather.bean.ProvinceBean;

import java.util.List;

/**
 * Author：Koterwong，Data：2016/4/25.
 * Description:
 */
public interface CityView {

    void setProDatas(List<ProvinceBean> mDatas);

    void setCityDatas(List<CityBean> mDatas);

    void showProgressBar();

    void hideProgressBar();

    void showToast(String toast);

    void setTitle(String title);

}
