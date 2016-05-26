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
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //对ACTION_DOWN消费，否则ACTION_MOVE不会调用。
                getParent().requestDisallowInterceptTouchEvent(true);
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
