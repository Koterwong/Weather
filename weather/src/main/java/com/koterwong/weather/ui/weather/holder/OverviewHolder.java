package com.koterwong.weather.ui.weather.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.koterwong.weather.R;
import com.koterwong.weather.base.BaseHolder;
import com.koterwong.weather.bean.WeatherBean;

import butterknife.Bind;

/**
 * ================================================
 * Created By：Koterwong; Time: 2016/06/09 00:02
 * <p/>
 * Description:
 * =================================================
 */
public class OverviewHolder extends BaseHolder<WeatherBean.NowBean> {

    @Bind(R.id.apparent_temperature_value)
    TextView mTemValue;
    @Bind(R.id.humidity_value)
    TextView mHumValue;
    @Bind(R.id.precipitation_value)
    TextView mPcpnValue;
    @Bind(R.id.pressure_value)
    TextView mPreValue;
    @Bind(R.id.visibility_value)
    TextView mVisibilityValue;
    @Bind(R.id.wind_value)
    TextView mWindValue;

    public OverviewHolder(Context context) {
        super(context);
    }

    @Override protected View initView() {
        return mInflater.inflate(R.layout.view_overview, null, false);
    }

    @Override public void refreshView(WeatherBean.NowBean nowBean) {
        mTemValue.setText(String.format("%s °", nowBean.tmp));
        mHumValue.setText(nowBean.hum + " %");
        mPcpnValue.setText(nowBean.pcpn + " %");
        mPreValue.setText(String.format("%s mbar", nowBean.pres));
        mVisibilityValue.setText(String.format("%s km", nowBean.vis));
        mWindValue.setText(String.format("%s%s级", nowBean.wind.dir, nowBean.wind.sc));
    }
}
