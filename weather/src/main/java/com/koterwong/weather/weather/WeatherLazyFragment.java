package com.koterwong.weather.weather;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.koterwong.weather.R;
import com.koterwong.weather.beans.WeatherBean;
import com.koterwong.weather.weather.presenter.WeatherPresenter;
import com.koterwong.weather.weather.presenter.WeatherPresenterImp;
import com.koterwong.weather.weather.view.WeatherView;

import java.util.List;

/**
 * Author：Koterwong，Data：2016/4/27.
 * Description:
 */
public class WeatherLazyFragment extends BaseFragment implements WeatherView, View.OnClickListener {

    //UI reference
    private View mWeatherView;
    private RelativeLayout mLoadingRl;
    private LinearLayout mLoadError;
    private Button mRetryBtn;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ScrollView mScrollView;

    private ImageView mWeatherIcon;
    private TextView mWeatherDes;
    private TextView mWeatherTmp;
    private TextView mWeatherAqi;
    private TextView mWeatherHum;
    private TextView mWeatherWind;
    private TextView mWeatherUpdateTime;
    private FrameLayout mDailyForecastFl;
    private FrameLayout mSuggestFl;

    private String city;

    private WeatherPresenter mPresenter;
    private DailyForecastHolder mDailyHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            city = getArguments().getString("city");
        }
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mWeatherView = inflater.inflate(R.layout.weather_fragment, container, false);

        //loading pager
        mLoadingRl = (RelativeLayout) mWeatherView.findViewById(R.id.rl_load_weather);

        //loading success
        mSwipeRefreshLayout = (SwipeRefreshLayout) mWeatherView.findViewById(R.id.swipe_layout);
        initmSwipeRefreshLayout();
        mScrollView = (ScrollView) mWeatherView.findViewById(R.id.sl_content);

        //loading error
        mLoadError = (LinearLayout) mWeatherView.findViewById(R.id.ll_load_error);
        mRetryBtn = (Button) mWeatherView.findViewById(R.id.btn_retry_load);
        mRetryBtn.setOnClickListener(this);

        mWeatherIcon = (ImageView) mWeatherView.findViewById(R.id.weather_icon);
        mWeatherDes = (TextView) mWeatherView.findViewById(R.id.weather_des);
        mWeatherTmp = (TextView) mWeatherView.findViewById(R.id.weather_tmp);
        mWeatherAqi = (TextView) mWeatherView.findViewById(R.id.weather_api);
        mWeatherHum = (TextView) mWeatherView.findViewById(R.id.weather_humidity);
        mWeatherWind = (TextView) mWeatherView.findViewById(R.id.weather_wind);
        mWeatherUpdateTime = (TextView) mWeatherView.findViewById(R.id.weather_update_time);

        mDailyForecastFl = (FrameLayout) mWeatherView.findViewById(R.id.fl_daily_forecast);
        addDailyHolder();
        mSuggestFl = (FrameLayout) mWeatherView.findViewById(R.id.fl_sug);
        addSuggestHolder();
        return mWeatherView;
    }

    @Override
    protected void initData() {
        //加载天气数据，无需关心本地OR NETWORK
        mPresenter = new WeatherPresenterImp(this);
        mPresenter.loadWeatherData(city);
    }

    /*添加未来天气预报的Holder*/
    private void addDailyHolder() {
        mDailyHolder = new DailyForecastHolder();
        View mDailyView =  mDailyHolder.getmForecastView();
        mDailyForecastFl.addView(mDailyView);
    }

    /*刷新未来天气*/
    @Override
    public void refreshWeatherDaily(List<WeatherBean.DailyForecastBean> mDailyForecastList) {
        mDailyHolder.refreshView(mDailyForecastList);
    }

    /*添加生活建议的Holder*/
    private void addSuggestHolder() {

    }
    private void initmSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary,
                R.color.primary_light,
                R.color.accent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadWeatherData(city);
            }
        });
    }

    /*判断SwipeRefreshLayout的状态*/
    @Override
    public boolean isSwipeRefreshLayoutRefreshing() {
        return mSwipeRefreshLayout.isRefreshing();
    }

    /*设置SwipeRefresh的状态*/
    @Override
    public void setmSwipeRefreshLayoutStatue(boolean statue) {
        mSwipeRefreshLayout.setRefreshing(statue);
    }

    @Override
    public void setmWeatherIcon(int ResId) {
        mWeatherIcon.setImageResource(ResId);
    }

    @Override
    public void setmWeatherDes(String mWeatherDes) {
        this.mWeatherDes.setText(mWeatherDes);
    }

    @Override
    public void setmWeatherTmp(String mWeatherTmp) {
        this.mWeatherTmp.setText(mWeatherTmp);
    }

    @Override
    public void setmWeatherAqi(String mWeatherAqi) {
        this.mWeatherAqi.setText(mWeatherAqi);
    }

    @Override
    public void setmWeatherHum(String mWeatherHum) {
        this.mWeatherHum.setText(mWeatherHum);
    }

    @Override
    public void setmWeatherWind(String mWeatherWind) {
        this.mWeatherWind.setText(mWeatherWind);
    }

    @Override
    public void setmWeatherUpdateTime(String mUTime) {
        this.mWeatherUpdateTime.setText(mUTime);
    }

    @Override
    public void showToastMsg(String msg) {
        Snackbar.make(mWeatherView, msg, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 显示加载中界面
     */
    @Override
    public void showLoadingVisible() {
        mLoadingRl.setVisibility(View.VISIBLE);
        mLoadError.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
    }
    /**
     * 显示加载成功界面
     */
    @Override
    public void showSuccessVisible() {
        mLoadingRl.setVisibility(View.GONE);
        mLoadError.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
    }
    /**
     * 显示加载失败界面
     */
    @Override
    public void showErrorVisible() {
        mLoadingRl.setVisibility(View.GONE);
        mLoadError.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        mPresenter.loadWeatherData(city);
    }
}
