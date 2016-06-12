package com.koterwong.weather.ui.choicecity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.koterwong.weather.R;

/**
 * ================================================
 * Created By：Koterwong; Time: 2016/06/12 08:42
 * <p/>
 * Description:
 * =================================================
 */
public class CityListHolder extends RecyclerView.ViewHolder {

    public TextView mTextView;

    public CityListHolder(View view) {
        super(view);
        mTextView = (TextView) itemView.findViewById(R.id.tv_city);
    }
}