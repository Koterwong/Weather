package com.koterwong.weather.ui.weather;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koterwong.weather.R;
import com.koterwong.weather.base.BaseFragment;
import com.koterwong.weather.beans.WeatherBean;
import com.koterwong.weather.ui.weather.holder.OverviewHolder;
import com.koterwong.weather.ui.weather.holder.DailyForecastHolder;
import com.koterwong.weather.ui.weather.holder.IndexHolder;
import com.koterwong.weather.ui.weather.presenter.WeatherPresenter;
import com.koterwong.weather.ui.weather.presenter.WeatherPresenterImp;
import com.koterwong.weather.ui.weather.view.WeatherView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author：Koterwong，Data：2016/4/27.
 * Description:
 */
public class WeatherLazyFragment extends BaseFragment implements WeatherView, View.OnClickListener {

    private String city;
    private WeatherPresenter mPresenter;
    //UI reference
    private View mWeatherView;
    private RelativeLayout mLoadingRl;
    private LinearLayout mLoadError;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ImageView mWeatherIcon;
    private TextView mWeatherDes;
    private TextView mWeatherTmp;

    private CardView mDailyCard;
    private CardView mSuggestCard;
    private CardView mBasicCard;

    private DailyForecastHolder mDailyHolder;
    private OverviewHolder mOverviewHolder;
    private IndexHolder mIndexHolder;

    @Bind(R.id.weather_update_time)
    TextView mUpdateTv;
    @Bind(R.id.weather_pm25)
    TextView mPm25;

    public static Fragment newInstance(String city){
        Bundle bundle = new Bundle();
        bundle.putString("city",city);
        WeatherLazyFragment weatherLazyFragment = new WeatherLazyFragment();
        weatherLazyFragment.setArguments(bundle);
        return weatherLazyFragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            city = getArguments().getString("city");
        }
    }

    @Override protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mWeatherView = inflater.inflate(R.layout.fragment_weather, container, false);
        //loading pager
        mLoadingRl = (RelativeLayout) mWeatherView.findViewById(R.id.rl_load_weather);
        //loaded success pager
        mSwipeRefreshLayout = (SwipeRefreshLayout) mWeatherView.findViewById(R.id.swipe_layout);
        this.initmSwipeRefreshLayout();
        //loaded error pager
        mLoadError = (LinearLayout) mWeatherView.findViewById(R.id.ll_load_error);
        Button mRetryBtn = (Button) mWeatherView.findViewById(R.id.btn_retry_load);
        mRetryBtn.setOnClickListener(this);

        //card1
        ((TextView) mWeatherView.findViewById(R.id.weather_city)).setText(city);
        mWeatherDes = (TextView) mWeatherView.findViewById(R.id.weather_des);
        mWeatherIcon = (ImageView) mWeatherView.findViewById(R.id.weather_icon);
        mWeatherTmp = (TextView) mWeatherView.findViewById(R.id.weather_tmp);
        //card2
        mDailyCard = (CardView) mWeatherView.findViewById(R.id.card_daily_forecast);
        this.addDailyHolder();
        //card3
        mBasicCard = (CardView) mWeatherView.findViewById(R.id.card_basic);
        this.addOverviewHolder();
        //card4
        mSuggestCard = (CardView) mWeatherView.findViewById(R.id.card_sug);
        this.addSuggestHolder();
        ButterKnife.bind(this,mWeatherView);
        return mWeatherView;
    }

    @Override protected void initData() {
        //加载天气数据，无需关心本地或者网络
        mPresenter = new WeatherPresenterImp(this);
        mPresenter.loadWeatherData(city);
    }

    private void initmSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary,
                R.color.white,
                R.color.accent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                mPresenter.loadWeatherData(city);
            }
        });
    }

    private void addDailyHolder() {
        mDailyHolder = new DailyForecastHolder(getActivity().getApplicationContext());
        mDailyCard.addView(mDailyHolder.getContentView());
    }

    private void addSuggestHolder() {
        mIndexHolder = new IndexHolder(getActivity().getApplicationContext());
        mSuggestCard.addView(mIndexHolder.getContentView());
    }

    private void addOverviewHolder() {
        mOverviewHolder = new OverviewHolder(getActivity().getApplicationContext());
        mBasicCard.addView(mOverviewHolder.getContentView());
    }

    @Override public void refreshDailyView(List<WeatherBean.DailyForecastBean> mDailyForecastList) {
        mDailyHolder.refreshView(mDailyForecastList);
    }

    @Override public void refreshSuggestView(WeatherBean.SuggestionBean suggestionBean) {
        mIndexHolder.refreshView(suggestionBean);
    }

    @Override public void refreshOverview(WeatherBean.NowBean nowBean) {
        mOverviewHolder.refreshView(nowBean);
    }

    @Override public boolean isSwipeRefreshLayoutRefreshing() {
        return mSwipeRefreshLayout.isRefreshing();
    }

    @Override public void setSwipeRefreshLayoutStatue(boolean statue) {
        mSwipeRefreshLayout.setRefreshing(statue);
    }

    @Override public void setWeatherIcon(int ResId) {
        mWeatherIcon.setImageResource(ResId);
    }

    @Override public void setWeatherDes(String mWeatherDes) {
        this.mWeatherDes.setText(mWeatherDes);
    }

    @Override public void setWeatherTmp(String mWeatherTmp) {
        this.mWeatherTmp.setText(mWeatherTmp);
    }

    @Override public void setWeatherPm25(String PM25) {
        this.mPm25.setText(PM25);
    }

    @Override public void setWeatherUpdateTime(String time) {
        mUpdateTv.setText(time);
    }

    @Override public void showToastMsg(String msg) {
        Snackbar.make(mWeatherView, msg, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 显示加载中界面
     */
    @Override public void showLoadingVisible() {
        mLoadingRl.setVisibility(View.VISIBLE);
        mLoadError.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
    }

    /**
     * 显示加载成功界面
     */
    @Override public void showSuccessVisible() {
        mLoadingRl.setVisibility(View.GONE);
        mLoadError.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 显示加载失败界面
     */
    @Override public void showErrorVisible() {
        mLoadingRl.setVisibility(View.GONE);
        mLoadError.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
    }

    @Override public void onClick(View v) {
        mPresenter.loadWeatherData(city);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        mPresenter = null;
        ButterKnife.unbind(this);
        mDailyHolder.unBindView();
        mIndexHolder.unBindView();
        mOverviewHolder.unBindView();
    }
}
