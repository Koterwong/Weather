package com.koterwong.weather.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author：Koterwong，
 * Date：2016/5/22 08:50
 * Description:
 */
public class KKBorderDividerItemDecoration extends RecyclerView.ItemDecoration{
    private final int verticalItemSpacingInPx;
    private final int horizontalItemSpacingInPx;


    public KKBorderDividerItemDecoration(int verticalItemSpacingInPx, int horizontalItemSpacingInPx) {
        this.verticalItemSpacingInPx = verticalItemSpacingInPx;
        this.horizontalItemSpacingInPx = horizontalItemSpacingInPx;
    }


    /**
     * set border
     *
     * @param outRect outRect
     * @param view view
     * @param parent parent
     * @param state state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        int itemPosition = layoutParams.getViewLayoutPosition();
        int childCount = parent.getAdapter().getItemCount();

        int left = this.horizontalItemSpacingInPx;
        int right = this.horizontalItemSpacingInPx;

        int top = getItemTopSpacing(itemPosition);
        int bottom = getItemBottomSpacing(itemPosition, childCount);
        outRect.set(left, top, right, bottom);
    }


    /**
     * get the item bottom spacing
     *
     * @param itemPosition itemPosition
     * @param childCount childCount
     * @return int
     */
    private int getItemBottomSpacing(int itemPosition, int childCount) {
        if (isLastItem(itemPosition, childCount)) {
            return this.verticalItemSpacingInPx;
        }
        return this.verticalItemSpacingInPx / 2;
    }


    /**
     * get the item top spacing
     *
     * @param itemPosition itemPosition
     * @return int
     */
    private int getItemTopSpacing(int itemPosition) {
        if (isFirstItem(itemPosition)) {
            return this.verticalItemSpacingInPx;
        }
        return this.verticalItemSpacingInPx / 2;
    }


    /**
     * is the first item
     *
     * @param itemPosition itemPosition
     * @return boolean
     */
    private boolean isFirstItem(int itemPosition) {
        return itemPosition == 0;
    }


    /**
     * is the last item
     *
     * @param itemPosition itemPosition
     * @param childCount childCount
     * @return boolean
     */
    private boolean isLastItem(int itemPosition, int childCount) {
        return itemPosition == childCount - 1;
    }
}
