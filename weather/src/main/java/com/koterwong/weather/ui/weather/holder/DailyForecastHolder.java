package com.koterwong.weather.ui.weather.holder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.koterwong.weather.R;
import com.koterwong.weather.base.BaseHolder;
import com.koterwong.weather.bean.WeatherBean;
import com.koterwong.weather.ui.weather.WeatherJsonUtil;
import com.koterwong.weather.utils.CalendarUtils;
import com.koterwong.weather.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Author：Koterwong，Data：2016/4/28.
 * Description:
 */
public class DailyForecastHolder extends BaseHolder<List<WeatherBean.DailyForecastBean>> {

    private TextView[] mTextViews;
    private ImageView[] mImageViews;
    private LineChartView mChart;

    public DailyForecastHolder(Context context) {
        super(context);
    }

    @Override protected View initView() {
        mContentView = mInflater.inflate(R.layout.view_weather_forecast, null, false);
        mTextViews = new TextView[6];
        mTextViews[0] = (TextView) mContentView.findViewById(R.id.weather_week1);
        mTextViews[1] = (TextView) mContentView.findViewById(R.id.weather_week2);
        mTextViews[2] = (TextView) mContentView.findViewById(R.id.weather_week3);
        mTextViews[3] = (TextView) mContentView.findViewById(R.id.weather_week4);
        mTextViews[4] = (TextView) mContentView.findViewById(R.id.weather_week5);
        mTextViews[5] = (TextView) mContentView.findViewById(R.id.weather_week6);
        mImageViews = new ImageView[6];
        mImageViews[0] = (ImageView) mContentView.findViewById(R.id.weather_icon1);
        mImageViews[1] = (ImageView) mContentView.findViewById(R.id.weather_icon2);
        mImageViews[2] = (ImageView) mContentView.findViewById(R.id.weather_icon3);
        mImageViews[3] = (ImageView) mContentView.findViewById(R.id.weather_icon4);
        mImageViews[4] = (ImageView) mContentView.findViewById(R.id.weather_icon5);
        mImageViews[5] = (ImageView) mContentView.findViewById(R.id.weather_icon6);
        mChart = (LineChartView) mContentView.findViewById(R.id.chart);
        return mContentView;
    }


    @Override public void refreshView(List<WeatherBean.DailyForecastBean> mDailyForecastList) {
        String week;
        for (int i = 0; i < mTextViews.length; i++) {
            if (i == 0) {
                week = "今日";
            } else if (i == 1) {
                week = "明日";
            } else {
                week = DateUtils.dayForWeek(mDailyForecastList.get(i).date);
            }
            mTextViews[i].setText(week);
            int resId = 0;
            if (CalendarUtils.isDay()){
                resId = WeatherJsonUtil.getWeatherImage(mDailyForecastList.get(i).cond.txt_d);
            }else{
                resId = WeatherJsonUtil.getWeatherImage(mDailyForecastList.get(i).cond.txt_n);
            }
            mImageViews[i].setImageResource(resId);
        }
        //刷新chart折线表
        List<PointValue> mPointValuesTemMax = new ArrayList<>();
        List<PointValue> mPointValuesTemMin = new ArrayList<>();
        for (int i = 0; i < mImageViews.length; i++) {
            mPointValuesTemMax.add(new PointValue(i, string2Float(mDailyForecastList.get(i).tmp.max)));
            mPointValuesTemMin.add(new PointValue(i, string2Float(mDailyForecastList.get(i).tmp.min)));
        }
        //初始化线和点
        Line mLineMax = new Line(mPointValuesTemMax);
        Line mLineMin = new Line(mPointValuesTemMin);

        List<Line> mLines = new ArrayList<>();
        mLines.add(mLineMax);
        mLines.add(mLineMin);
        //setColor(R.color.primary)该方法设置Color不起作用
        mLineMax.setColor(mContext.getResources().getColor(R.color.teal_300))
                .setCubic(false)
                .setPointColor(mContext.getResources().getColor(R.color.teal_200))
                .setPointRadius(3)
                .setStrokeWidth(2)
                .setHasLabels(true);
        mLineMin.setColor(mContext.getResources().getColor(R.color.teal_300))
                .setCubic(false)
                .setPointColor(mContext.getResources().getColor(R.color.teal_200))
                .setPointRadius(3)
                .setStrokeWidth(2)
                .setHasLabels(true);

        LineChartData mChartData = new LineChartData();
        mChartData.setLines(mLines);
        mChartData.setValueLabelTextSize(10);
        mChartData.setValueLabelsTextColor(mContext.getResources().getColor(R.color.teal_300));
        mChartData.setValueLabelBackgroundAuto(false);
        mChartData.setValueLabelBackgroundColor(Color.TRANSPARENT);
        mChartData.setAxisXBottom(null);
        mChartData.setAxisYLeft(null);
        //设置不可交互
//        mChart.setInteractive(true);
        mChart.setClickable(true);
        //设置不可缩放
        mChart.setZoomEnabled(false);
        mChart.setLineChartData(mChartData);
    }

    private float string2Float(String value) {
        return Float.parseFloat(value);
    }
}
