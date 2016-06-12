package com.koterwong.weather.ui.choicecity.presenter;

import com.koterwong.weather.beans.CityBean;
import com.koterwong.weather.beans.ProvinceBean;
import com.koterwong.weather.ui.choicecity.Model.CityModel;
import com.koterwong.weather.ui.choicecity.Model.CityModelImp;
import com.koterwong.weather.ui.choicecity.View.CityView;

import java.util.List;

/**
 * Author：Koterwong，Data：2016/4/25.
 * Description:
 */
public class CityPresenterImp implements CityPresenter, CityModelImp.QueryProListener, CityModelImp.QueryCityListener {

    private CityView mCityView;
    private CityModel mCityModel;

    public CityPresenterImp(CityView mCityView) {
        this.mCityView = mCityView;
        mCityModel = new CityModelImp();
    }

    @Override public void queryProvince() {
        mCityView.showProgressBar();
        mCityModel.queryProvince(this);
    }

    @Override public void queryCity(String proID) {
        mCityModel.queryCity(proID, this);
    }

    @Override public void queryProvinceSuccess(final List<ProvinceBean> provinceList) {
        mCityView.setProDatas(provinceList);
        mCityView.hideProgressBar();
    }

    @Override public void queryProvinceFailed(Exception e) {
        mCityView.showToast(e.toString());
    }

    @Override public void queryCitySuccess(List<CityBean> cityList) {
        mCityView.setCityDatas(cityList);
    }

    @Override public void queryCityFailed(Exception e) {
        mCityView.showToast(e.toString());
    }
}
