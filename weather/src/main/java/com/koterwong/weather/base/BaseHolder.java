package com.koterwong.weather.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;

/**
 * ================================================
 * Created By：Koterwong; Time: 2016/06/09 06:12
 * <p>
 * Description:天气界面的CardView内容。
 * =================================================
 */
public abstract class BaseHolder<Data> {

    protected LayoutInflater mInflater;
    protected View mContentView;
    protected Context mContext;

    public BaseHolder(Context context){
        this.mContext = context;
        this.mInflater  = LayoutInflater.from(context);
        this.mContentView = initView();
        ButterKnife.bind(this,mContentView);
    }

    protected abstract View initView();

    public View getContentView(){
        return mContentView;
    }

    public abstract void refreshView(Data data);

    public void unBindView(){
        ButterKnife.unbind(this);
    }
}
