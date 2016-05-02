package com.koterwong.weather.weather;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.koterwong.weather.base.BaseApplication;
import com.koterwong.weather.beans.WeatherBean;
import com.koterwong.weather.R;
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
public class DailyForecastHolder {

    private final View mForecastView;

    private TextView[] mTextViews;
    private ImageView[] mImageViews;
    private LineChartView mChart;

    public DailyForecastHolder() {
        mForecastView = LayoutInflater.from(BaseApplication.getApplication()).inflate(R.layout.weather_forecast,
                null, false);
        mTextViews = new TextView[5];
        mTextViews[0] = (TextView) mForecastView.findViewById(R.id.weather_week1);
        mTextViews[1] = (TextView) mForecastView.findViewById(R.id.weather_week2);
        mTextViews[2] = (TextView) mForecastView.findViewById(R.id.weather_week3);
        mTextViews[3] = (TextView) mForecastView.findViewById(R.id.weather_week4);
        mTextViews[4] = (TextView) mForecastView.findViewById(R.id.weather_week5);
        mImageViews = new ImageView[5];
        mImageViews[0] = (ImageView) mForecastView.findViewById(R.id.weather_icon1);
        mImageViews[1] = (ImageView) mForecastView.findViewById(R.id.weather_icon2);
        mImageViews[2] = (ImageView) mForecastView.findViewById(R.id.weather_icon3);
        mImageViews[3] = (ImageView) mForecastView.findViewById(R.id.weather_icon4);
        mImageViews[4] = (ImageView) mForecastView.findViewById(R.id.weather_icon5);

        mChart = (LineChartView) mForecastView.findViewById(R.id.chart);
    }

    public View getmForecastView() {
        if (mForecastView != null) {
            return mForecastView;
        }
        return new DailyForecastHolder().getmForecastView();
    }

    public void refreshView(List<WeatherBean.DailyForecastBean> mDailyForecastList) {
        String week;
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                week = "今日";
            } else if (i == 1) {
                week = "明日";
            } else {
                week = DateUtils.dayForWeek(mDailyForecastList.get(i).date);
            }
            mTextViews[i].setText(week);
            int imfID = WeatherJsonUtil.getWeatherImage(mDailyForecastList.get(i).cond.txt_d);
            mImageViews[i].setImageResource(imfID);
        }
        //刷新chart折线表
        List<PointValue> mPointValuestemMax = new ArrayList<>();
        List<PointValue> mPointValuestemMin = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mPointValuestemMax.add(new PointValue(i, string2Float(mDailyForecastList.get(i).tmp.max)));
            mPointValuestemMin.add(new PointValue(i, string2Float(mDailyForecastList.get(i).tmp.min)));
        }
        //初始化线和点
        Line mLineMax = new Line(mPointValuestemMax);
        Line mLineMin = new Line(mPointValuestemMin);

        List<Line> mLines = new ArrayList<>();
        mLines.add(mLineMax);
        mLines.add(mLineMin);
        //setColor(R.color.primary)该方法设置Color不起作用
        mLineMax.setColor(BaseApplication.getApplication().getResources().getColor(R.color.primary))
                .setCubic(false)
                .setPointColor(BaseApplication.getApplication().getResources().getColor(R.color.primary))
                .setPointRadius(3)
                .setStrokeWidth(2)
                .setHasLabels(true);
        mLineMin.setColor(BaseApplication.getApplication().getResources().getColor(R.color.primary))
                .setCubic(false)
                .setPointColor(BaseApplication.getApplication().getResources().getColor(R.color.primary))
                .setPointRadius(3)
                .setStrokeWidth(2)
                .setHasLabels(true);

        LineChartData mChartData = new LineChartData();
        mChartData.setLines(mLines);
        mChartData.setValueLabelTextSize(10);
        mChartData.setValueLabelsTextColor(BaseApplication.getApplication().getResources().getColor(R.color.primary));
        mChartData.setValueLabelBackgroundAuto(false);
        mChartData.setValueLabelBackgroundColor(Color.TRANSPARENT);
        mChartData.setAxisXBottom(null);
        mChartData.setAxisYLeft(null);
        //设置不可交互
//        mChart.setInteractive(true);
        //设置不可缩放
        mChart.setZoomEnabled(false);
        mChart.setLineChartData(mChartData);
    }

    public float string2Float(String value) {
        return Float.parseFloat(value);
    }
}
