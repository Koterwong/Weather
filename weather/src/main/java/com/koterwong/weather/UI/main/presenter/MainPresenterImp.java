package com.koterwong.weather.ui.main.presenter;

import android.app.Activity;

import com.koterwong.weather.MyApp;
import com.koterwong.weather.R;
import com.koterwong.weather.commons.database.SavedCityDBManager;
import com.koterwong.weather.commons.SettingPref;
import com.koterwong.weather.ui.about.AboutActivity;
import com.koterwong.weather.ui.choicecity.ChoiceCityActivity;
import com.koterwong.weather.ui.main.MainActivity2;
import com.koterwong.weather.ui.main.model.MainModelImpl;
import com.koterwong.weather.ui.main.view.MainView;
import com.koterwong.weather.ui.managercity.ManagerCityActivity;
import com.koterwong.weather.ui.setting.SettingsActivity;
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
                        mMainView.switch2ActivityForResult(ChoiceCityActivity.class, MainActivity2.REQUEST_CODE_ADD);
                        break;
                    case R.id.nav_manager_city:
                        mMainView.switch2Activity(ManagerCityActivity.class);
                        break;
                    case R.id.nav_setting:
                        mMainView.switch2Activity(SettingsActivity.class);
                        break;
                    case R.id.nav_about:
                        mMainView.switch2Activity(AboutActivity.class);
                        break;
                }
            }
        }, 240);
    }

    @Override public void loadCities() {
        SavedCityDBManager mDatabase = SavedCityDBManager.getInstance((Activity) mMainView);
        List<String> mCityList = mDatabase.queryCities();
        if (mCityList != null && mCityList.size() > 0) {
            mMainView.setContentVisible(true);
            mMainView.setCities(mCityList);
        } else {
            mMainView.setContentVisible(false);
            //请求定位权限。
            RxPermissions.getInstance((Activity) mMainView)
                    .request(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe(new Action1<Boolean>() {
                        @Override public void call(Boolean aBoolean) {
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
