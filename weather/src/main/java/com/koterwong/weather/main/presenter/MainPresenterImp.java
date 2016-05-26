package com.koterwong.weather.main.presenter;

import android.app.Activity;

import com.koterwong.weather.R;
import com.koterwong.weather.base.BaseApplication;
import com.koterwong.weather.commons.Setting;
import com.koterwong.weather.commons.SavedCityDBManager;
import com.koterwong.weather.main.model.MainModel;
import com.koterwong.weather.main.view.MainView;

import java.util.List;

/**
 * Author：Koterwong，Data：2016/4/27.
 * Description:
 */
public class MainPresenterImp implements MainPresenter {

    private MainView mMainView;

    public MainPresenterImp(MainView mMainView) {
        this.mMainView = mMainView;
    }

    @Override
    public void switchNavigation(final int position) {
        BaseApplication.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (position) {
                    case R.id.nav_choice_city:
                        mMainView.switch2ChoiceCityActivity();
                        break;
                    case R.id.nav_manager_city:
                        mMainView.switch2ManagerCityActivity();
                        break;
                    case R.id.nav_setting:
                        mMainView.switch2SettingActivity();
                        break;
                    case R.id.nav_about:
                        mMainView.switch2AboutActivity();
                        break;
                }
            }
        }, 220);
    }

    @Override
    public void loadCities() {
        SavedCityDBManager mDatabase = SavedCityDBManager.getInstance((Activity) mMainView);
        List<String> mCityList = mDatabase.queryCities();
        if (mCityList != null && mCityList.size() > 0) {
            //更新界面，设置数据给MainView
            mMainView.setContentVisible(true);
            mMainView.setCities(mCityList);
        } else {
            //没有城市数据，更新界面
            mMainView.setContentVisible(false);
            //判断是否允许定位
            if (Setting.getBoolean(Setting.IS_ALLOW_LOCATION, true)) {
                location();
                mMainView.setToolbarTitle("正在定位...");
            }
        }
    }

    /**
     * 定位城市。
     */
    private void location() {
        final MainModel mainModel = new MainModel();
        mainModel.locationCity(new MainModel.LocationListener() {
            @Override
            public void locationSuccess(String city) {
                mMainView.addCity(city);
                /*添加到数据库*/
                addCity(city);
            }

            @Override
            public void locationError() {
                mMainView.setToolbarTitle("定位失败");
            }
        });
    }

    @Override
    public void addCity(String cityName) {
        SavedCityDBManager mDatabase = SavedCityDBManager.getInstance((Activity) mMainView);
        mDatabase.addCity(cityName);
    }

    @Override
    public void deleteCity(String cityName) {
        SavedCityDBManager mDatabase = SavedCityDBManager.getInstance((Activity) mMainView);
        mDatabase.deleteCity(cityName);
    }
}
