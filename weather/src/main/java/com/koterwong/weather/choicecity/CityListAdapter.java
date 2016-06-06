package com.koterwong.weather.choicecity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koterwong.weather.BaseApplication;
import com.koterwong.weather.beans.CityBean;
import com.koterwong.weather.beans.ProvinceBean;
import com.koterwong.weather.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Koterwong，Data：2016/4/25.
 * Description:
 */
public class CityListAdapter extends RecyclerView.Adapter<CityListHolder> {

    List<ProvinceBean> mProDatas = new ArrayList<>();
    List<CityBean> mCityDatas = new ArrayList<>();
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static int currentLevel = LEVEL_PROVINCE;

    public void setmProDatas(List<ProvinceBean> mProDatas) {
        if (this.mProDatas.size() != 0) {
            this.mProDatas.clear();
        }
        currentLevel = LEVEL_PROVINCE;
        this.mProDatas = mProDatas;
        this.notifyDataSetChanged();
    }

    public void setmCityDatass(List<CityBean> mCityDatas) {
        if (this.mCityDatas.size() != 0) {
            this.mCityDatas.clear();
        }
        currentLevel = LEVEL_CITY;
        this.mCityDatas = mCityDatas;
        this.notifyDataSetChanged();
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    interface OnItemClickListener {
        void onClick(View itemView, int position, String name, String id, int Level);
    }

    @Override
    public CityListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(BaseApplication.getApplication()).
                inflate(R.layout.item_city, parent, false);
        return new CityListHolder(inflate);
    }

    @Override
    public void onBindViewHolder(CityListHolder holder, final int position) {
        if (currentLevel == LEVEL_PROVINCE) {
            holder.mTextView.setText(mProDatas.get(position).ProName);
            if (listener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClick(v, position, mProDatas.get(position).ProName, mProDatas.get(position).ProShort, LEVEL_PROVINCE);
                    }
                });
            }
        } else {
            holder.mTextView.setText(mCityDatas.get(position).CityName);
            if (listener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClick(v, position, mCityDatas.get(position).CityName, "0", LEVEL_CITY);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        if (currentLevel == LEVEL_PROVINCE) {
            if (mProDatas != null && mProDatas.size() > 0) {
                return mProDatas.size();
            }
        } else if (currentLevel == LEVEL_CITY) {
            if (mCityDatas != null && mCityDatas.size() > 0) {
                return mCityDatas.size();
            }
        }
        return 0;
    }
}

class CityListHolder extends RecyclerView.ViewHolder {

    public TextView mTextView;

    public CityListHolder(View view) {
        super(view);
        mTextView = (TextView) itemView.findViewById(R.id.tv_city);
    }

}