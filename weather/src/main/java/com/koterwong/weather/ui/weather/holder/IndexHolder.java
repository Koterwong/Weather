package com.koterwong.weather.ui.weather.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.koterwong.weather.R;
import com.koterwong.weather.base.BaseHolder;
import com.koterwong.weather.beans.WeatherBean;

import butterknife.Bind;

/**
 * Author：Koterwong，Data：2016/4/28.
 * Description:
 */
public class IndexHolder extends BaseHolder<WeatherBean.SuggestionBean> {

    @Bind(R.id.index_dress_title)
    TextView mDressTitle;
    @Bind(R.id.index_dress_detail)
    TextView mDressDetail;
    @Bind(R.id.index_sport_title)
    TextView mSportTitle;
    @Bind(R.id.index_sport_detail)
    TextView mSportDetail;
    @Bind(R.id.index_sun_title)
    TextView mSunTitle;
    @Bind(R.id.index_sun_detail)
    TextView mSunDetail;
    @Bind(R.id.index_fluen_title)
    TextView mFluenTitle;
    @Bind(R.id.index_fluen_detail)
    TextView mFluenDetail;
    @Bind(R.id.index_travel_title)
    TextView mTravelTitle;
    @Bind(R.id.index_travel_detail)
    TextView mTravelDetail;
    @Bind(R.id.index_car_wash_title)
    TextView mCarWashTitle;
    @Bind(R.id.index_car_wash_detail)
    TextView mCarWashDetail;


    public IndexHolder(Context context) {
        super(context);
    }

    @Override protected View initView() {
        return mInflater.inflate(R.layout.view_suggestion,null,false);
    }

    @Override public void refreshView(WeatherBean.SuggestionBean suggestionBean) {
        mDressTitle.setText(String.format("穿衣指数>>%s",suggestionBean.drsg.brf));
        mSportTitle.setText(String.format("运动指数>>%s",suggestionBean.sport.brf));
        mSunTitle.setText(String.format("防晒指数>>%s",suggestionBean.uv.brf));
        mFluenTitle.setText(String.format("感冒指数>>%s",suggestionBean.flu.brf));
        mTravelTitle.setText(String.format("旅游指数>>%s",suggestionBean.trav.brf));
        mCarWashTitle.setText(String.format("洗车指数>>%s",suggestionBean.cw.brf));

        mDressDetail.setText(suggestionBean.drsg.txt);
        mSportDetail.setText(suggestionBean.sport.txt);
        mSunDetail.setText(suggestionBean.uv.txt);
        mFluenDetail.setText(suggestionBean.flu.txt);
        mTravelDetail.setText(suggestionBean.trav.txt);
        mCarWashDetail.setText(suggestionBean.cw.txt);
    }
}
