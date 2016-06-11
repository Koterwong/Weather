package com.koterwong.weather.ui.main.presenter;

import android.app.Activity;

import com.koterwong.weather.MyApp;
import com.koterwong.weather.R;
import com.koterwong.weather.commons.SavedCityDBManager;
import com.koterwong.weather.commons.SettingPref;
import com.koterwong.weather.ui.main.model.MainModelImpl;
import com.koterwong.weather.ui.main.view.MainView;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import rx.functions.Action1;

/**
 * Author：Koterwong，Data：2016/4/27.
 * Description:
 */
public class MainPresenterImp implements MainPresenter {

    private MainView mMainView;

    public MainPresenterImp(MainView mMainView) {
        this.mMainView = mMainView;
    }

    @Override public void switchNavigation(final int position) {
        MyApp.getHandler().postDelayed(new Runnable() {
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
        }, 240);
    }

    @Override public void loadCities() {
        SavedCityDBManager mDatabase = SavedCityDBManager.getInstance((Activity) mMainView);
        List<String> mCityList = mDatabase.queryCities();
        if (mCityList != null && mCityList.size() > 0) {
            //更新界面，设置数据给MainView
            mMainView.setContentVisible(true);
            mMainView.setCities(mCityList);
        } else {
            //没有城市数据，更新界面
            mMainView.setContentVisible(false);
            //请求定位权限。
            RxPermissions.getInstance((Activity) mMainView)
                    .request(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            if (aBoolean) {
                                SettingPref.putBoolean(SettingPref.IS_ALLOW_LOCATION, true);
                                MainPresenterImp.this.location();
                            } else {
                                SettingPref.putBoolean(SettingPref.IS_ALLOW_LOCATION, false);
                            }
                        }
                    });
        }
    }

    /**
     * 定位城市。
     */
    private void location() {
        final MainModelImpl mainModelImpl = new MainModelImpl();
        mainModelImpl.locationCity(new MainModelImpl.LocationListener() {
            @Override public void locationSuccess(String city) {
                mMainView.addCity(city);
                mMainView.setToolbarTitle(((Activity)mMainView).getResources().getString(R.string.app_name));
                MainPresenterImp.this.addCityToDB(city);
            }

            @Override public void locationError() {

            }
        });
    }

    @Override public void addCityToDB(String cityName) {
        SavedCityDBManager mDatabase = SavedCityDBManager.getInstance((Activity) mMainView);
        mDatabase.addCity(cityName);
    }

    @Override public void deleteCity(String cityName) {
        SavedCityDBManager mDatabase = SavedCityDBManager.getInstance((Activity) mMainView);
        mDatabase.deleteCity(cityName);
    }
}
