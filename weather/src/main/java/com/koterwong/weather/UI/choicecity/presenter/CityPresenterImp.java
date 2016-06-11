package com.koterwong.weather.ui.choicecity.presenter;

import com.koterwong.weather.MyApp;
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
public class CityPresenterImp implements CityPresenter, CityModelImp.LoadCityDBListener, CityModelImp.QueryProListener {

    private CityView mCityView;
    private CityModel mCityModel;

    public CityPresenterImp(CityView mCityView) {
        this.mCityView = mCityView;
        mCityModel = new CityModelImp();
    }

    /**
     * Copy数据库
      */
    @Override public void loadDataList() {
        //copy
        mCityView.showProgressBar();
        mCityModel.copyDatabase(this);
    }

    /**
     * 查询省份数据
     */
    @Override public void copySuccess() {
        mCityModel.queryProvince(this);
    }

    @Override public void copyFailed(Exception e) {
        mCityView.showToast(e.toString());
        mCityView.hideProgressBar();
    }

    /**
     * 查询省份成功的回调
     * @param provinceList 回调的List集合
     */
    @Override public void querySuccess(final List<ProvinceBean> provinceList) {
        if (MyApp.getMainId()==Thread.currentThread().getId()){
            mCityView.setProDatas(provinceList);
        }else{
            MyApp.getHandler().post(new Runnable() {
                @Override public void run() {
                    mCityView.setProDatas(provinceList);
                }
            });
        }
        mCityView.hideProgressBar();
    }

    @Override public void queryFailed(Exception e) {
        mCityView.showToast(e.toString());
    }

    /**
     * 查询城市
     * @param proID 省ID
     */
    @Override public void queryCity(String proID) {
        mCityModel.queryCity(proID, new CityModelImp.QueryCityListener() {
            @Override public void querySuccess(List<CityBean> cityList) {
                mCityView.setCityDatas(cityList);
            }

            @Override public void queryFailed(Exception e) {
                mCityView.showToast("没有数据");
            }
        });
    }
}
