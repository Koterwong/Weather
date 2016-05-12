package com.koterwong.weather.managercity;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Author：Koterwong，Data：2016/5/2.
 * Description:
 */
public class MyRecyclerView extends RecyclerView {

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    int startX;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        //不要拦截事件
//        getParent().requestDisallowInterceptTouchEvent(true);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);// 不要拦截,这样是为了保证ACTION_MOVE调用
                startX = (int) ev.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getRawX();
                if (endX - startX > 0) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
