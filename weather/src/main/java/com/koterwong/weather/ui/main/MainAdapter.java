package com.koterwong.weather.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * ================================================
 * Created By：Koterwong; Time: 2016/06/14 06:43
 * <p/>
 * Description:
 * =================================================
 */
public class MainAdapter extends FragmentStatePagerAdapter {

    private List<String> mCityList;

    public MainAdapter(FragmentManager fm, List<String> mCityList) {
        super(fm);
        this.mCityList = mCityList;
    }

    /**
     * 需求注意的是，MainActivity和MainAdapter管理的是一个对象，在这里添加就不要在MainActivity中添加。
     */
    public void addCity(String cityName) {
        this.mCityList.add(cityName);
        this.notifyDataSetChanged();
    }
    /**
     * 需求注意的是，MainActivity和MainAdapter管理的是一个对象，在这里删除就不要在MainActivity中删除。
     */
    public void deleteCity(String cityName) {
        this.mCityList.remove(cityName);
    }

    @Override public Fragment getItem(int position) {
        return FragmentFactory.createFragment(mCityList.get(position));
    }

    @Override public int getCount() {
        return mCityList == null ? 0 : mCityList.size();
    }

    /**
     * 由于ViewPager的预加载特性。
     * Fragment的视图会一致存在内存中，所有就会导致即使删除Fragment，还能弹出半的View，但是无法滑动
     * 过去的Bug。我们需要重写该方法，让其PagerAdapter.POSITION_NONE就可以测底删除Viewpager中的
     * Fragment。
     */
    @Override public int getItemPosition(Object object) {
        return FragmentPagerAdapter.POSITION_NONE;
    }
}
