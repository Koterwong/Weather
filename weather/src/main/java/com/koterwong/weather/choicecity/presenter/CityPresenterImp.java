package com.koterwong.weather.choicecity.presenter;

import com.koterwong.weather.BaseApplication;
import com.koterwong.weather.beans.CityBean;
import com.koterwong.weather.beans.ProvinceBean;
import com.koterwong.weather.choicecity.Model.CityModel;
import com.koterwong.weather.choicecity.Model.CityModelImp;
import com.koterwong.weather.choicecity.View.CityView;

import java.util.List;

/**
 * Author：Koterwong，Data：2016/4/25.
 * Description:
 */
public class CityPresenterImp implements CityPresenter, CityModelImp.LoadCityDBListener, CityModelImp.QueryProListener {

//    public static final int LEVEL_PROVINCE = 0;
//    public static final int LEVEL_CITY = 1;
//    public static int currentLevel =LEVEL_PROVINCE;

    private CityView mCityView;
    private CityModel mCityModel;

    public CityPresenterImp(CityView mCityView) {
        this.mCityView = mCityView;
        mCityModel = new CityModelImp();
    }

    /**
     * Copy数据库
      */
    @Override
    public void loadDataList() {
        //copy
        mCityView.showProgressBar();
        mCityModel.loadCityDataBase(this);
    }

    /**
     * 查询省份数据
     */
    @Override
    public void copySuccess() {
        mCityModel.queryProvince(this);
    }

    @Override
    public void copyFailed(Exception e) {
        mCityView.showToast(e.toString());
        mCityView.hideProgressBar();
    }

    /**
     * 查询省份成功的回调
     * @param provinceList 回调的List集合
     */
    @Override
    public void querySuccess(final List<ProvinceBean> provinceList) {
        if (BaseApplication.getMainId()==Thread.currentThread().getId()){
            mCityView.setProDatas(provinceList);
        }else{
            BaseApplication.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    mCityView.setProDatas(provinceList);
                }
            });
        }
        mCityView.hideProgressBar();
    }

    @Override
    public void queryFailed(Exception e) {
        mCityView.showToast(e.toString());
    }

    /**
     * 查询城市
     * @param proID 省ID
     */
    @Override
    public void queryCity(String proID) {
        mCityModel.queryCity(proID, new CityModelImp.QueryCityListener() {
            @Override
            public void querySuccess(List<CityBean> cityList) {
                mCityView.setCityDatas(cityList);
            }

            @Override
            public void queryFailed(Exception e) {
                mCityView.showToast("没有数据");
            }
        });
    }
}
