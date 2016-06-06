package com.koterwong.weather.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.koterwong.weather.R;
import com.koterwong.weather.choicecity.ChoiceCityActivity;
import com.koterwong.weather.commons.Setting;
import com.koterwong.weather.main.presenter.MainPresenter;
import com.koterwong.weather.main.presenter.MainPresenterImp;
import com.koterwong.weather.main.view.MainView;
import com.koterwong.weather.managercity.ManagerCityActivity;
import com.koterwong.weather.settingandabout.about.AboutActivity;
import com.koterwong.weather.settingandabout.setting.AutoUpdateService;
import com.koterwong.weather.settingandabout.setting.ServiceStatueUtils;
import com.koterwong.weather.settingandabout.setting.SettingsActivity;
import com.koterwong.weather.utils.L;
import com.koterwong.weather.utils.ShareUtils;
import com.koterwong.weather.weather.WeatherLazyFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author：Koterwong，Data：2016/4/27.
 * Description:
 */
public class MainActivity2 extends AppCompatActivity implements MainView, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private final int REQUEST_CODE_ADD = 0;
    private final int REQUEST_CODE_DELETE = 1;
    /**
     * 判断是否为，额外添加的Activity；
     */
    private boolean isAddCity = false;

    //UI reference
    private DrawerLayout mDrawer;
    private NavigationView mNvView;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mToggle;

    //RlContent
    private RelativeLayout mRlContent;
    private Button mChoiceBtn;

    //viewPager
    private ViewPager mViewPager;
    private List<String> mCityList;

    //mPresenter
    private MainPresenter mPresenter;

    //adapter
    private MainAdapter mAdapter;
    //mFragments
    private Map<String, Fragment> mFragments;

    private static final String TAG = "MainActivity2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        mPresenter = new MainPresenterImp(this);
        mPresenter.loadCities();
        //判断自动更新服务运行状态。服务不再运行，才去开启服务。
        if (!ServiceStatueUtils.isServiceRunning(this, "AutoUpdateService")) {
            if (Setting.getBoolean(Setting.IS_ALLOW_UPDATE, false)) {
                startService(new Intent(this, AutoUpdateService.class));
            }
        }
    }

    private void initView() {
        setContentView(R.layout.activity_main2);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mToggle);
        //Drawer的拉出隐藏，改变android.R.id.home的图标，并带有的动画效果。
        mToggle.syncState();
        mNvView = (NavigationView) findViewById(R.id.nav_view);
        if (mNvView != null) {
            mNvView.setNavigationItemSelectedListener(this);
        }
        //没有城市界面。
        mRlContent = (RelativeLayout) findViewById(R.id.rl_content_choice);
        mChoiceBtn = (Button) findViewById(R.id.btn_choice_city);
        mChoiceBtn.setOnClickListener(this);
        //viewPager
        mViewPager = (ViewPager) findViewById(R.id.main_view_pager);
        //设置viewPager的缓冲数俩个
        mViewPager.setOffscreenPageLimit(3);
        //设置ViewPager动画
        mViewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setToolbarTitle(mCityList.get(position));
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                L.d(TAG, "position:" + position + "----positionOffset:" + positionOffset + "----positionOffsetPixels" + positionOffsetPixels);
            }
        });
    }

    /**
     * 加载城市数据成功时调用，没有城市数据不会调用该方法。
     * 只会调用一次，取消onRestart()方法，从新查询数据的操作。
     *
     * @param mCityList 城市信息
     */
    @Override
    public void setCities(List<String> mCityList) {
        this.mCityList = mCityList;
        String cityName = mCityList.get(0);
        setToolbarTitle(cityName);
        /*设置标题，必须在setSupport之前，或者onResume（）*/
        setSupportActionBar(mToolbar);
        if (mAdapter == null) {
            //启动app，并且查询到城市，mAdapter==null
            mAdapter = new MainAdapter(getSupportFragmentManager());
            mViewPager.setAdapter(mAdapter);
        }
//        else{
//            //从其他界面跳转过来，mAdapter!=null,添加了城市
//            this.mCityList.clear();
//            this.mCityList = mCityList;
//            mAdapter.notifyDataSetChanged();
//        }
    }

    /**
     * 添加单个城市。
     *
     * @param cityName
     */
    @Override
    public void addCity(String cityName) {
        if (mAdapter == null) {
            //将天气界面值为可见。
            setContentVisible(true);
            //之前没有在数据库中查询到城市.通过定位或手动选择了城市。
            //设置标题
            setToolbarTitle(cityName);
            mCityList = new ArrayList<>();
            mCityList.add(cityName);
            mAdapter = new MainAdapter(getSupportFragmentManager());
            mViewPager.setAdapter(mAdapter);
        } else {
            //之前已经在数据中查询到城市，额外添加了城市
            mCityList.add(cityName);
            setContentVisible(true);
            mAdapter.addCity(cityName);
        }
    }

    /*设置Toolbar标题*/
    @Override
    public void setToolbarTitle(String title) {
        mToolbar.setTitle(title);
    }

    /**
     * 设置两种界面的显示方式
     *
     * @param visible true：天气界面显示 false：选择城市界面显示
     */
    @Override
    public void setContentVisible(boolean visible) {
        mViewPager.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        mRlContent.setVisibility(visible ? View.INVISIBLE : View.VISIBLE);
    }

    /*Nav点击事件的处理 */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        item.setChecked(true);
        mDrawer.closeDrawer(GravityCompat.START);
        mPresenter.switchNavigation(item.getItemId());
        return true;
    }


    /*跳转到选择城市的Activity*/
    @Override
    public void switch2ChoiceCityActivity() {
        Intent mIntent = new Intent(this, ChoiceCityActivity.class);
        this.startActivityForResult(mIntent, REQUEST_CODE_ADD);
    }

    /*跳转到管理城市的Activity*/
    @Override
    public void switch2ManagerCityActivity() {
        Intent mIntent = new Intent(this, ManagerCityActivity.class);
        this.startActivityForResult(mIntent, REQUEST_CODE_DELETE);
    }

    /*跳转到设置的Activity*/
    @Override
    public void switch2SettingActivity() {
        this.startActivity(new Intent(this, SettingsActivity.class));
    }

    /*跳转到关于的Activity*/
    @Override
    public void switch2AboutActivity() {
        this.startActivity(new Intent(this, AboutActivity.class));
    }

    /* 跳转到ChoiceActivity*/
    @Override
    public void onClick(View v) {
        Intent mIntent = new Intent(this, ChoiceCityActivity.class);
        startActivityForResult(mIntent, REQUEST_CODE_ADD);
    }

    /* 选择城市界面返回的结果*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CODE_ADD: //从没有城市数据到添加一个城市数据
                if (resultCode == RESULT_OK) {
                    String city = data.getStringExtra("city");
                    if (city == null) {
                        Snackbar.make(mRlContent, "添加失败啦~", Snackbar.LENGTH_LONG);
                        return;
                    }
                    this.addCity(city);
                    /*将城市保存到数据库*/
                    mPresenter.addCity(city);
                }
                break;
            case REQUEST_CODE_DELETE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    Set<String> keySet = bundle.keySet();
                    if (keySet.size() > 0) {
                        Iterator<String> iterator = keySet.iterator();
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            String deleteCity = bundle.getString(key);
                            /**
                             * 将Map中管理的Fragment删除。
                             */
                            mFragments.remove(deleteCity);
                            /**
                             * 同样将城市从集合中移除
                             */
                            mCityList.remove(deleteCity);
                        }
                        if (mFragments.size() == 0) {
                            setContentVisible(false);
                        }
                        mAdapter.notifyDataSetChanged();
                        /**
                         * 从新设置标题，修复标题和天气信息不一致bug。
                         */
                        setToolbarTitle(mCityList.get(mViewPager.getCurrentItem()));
                    }
                }
        }
//        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (mDrawer.isDrawerOpen(GravityCompat.START)){
                    mDrawer.closeDrawers();
                }else{
                    mDrawer.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.action_share:
                ShareUtils.share(MainActivity2.this,"推荐一款精美天气软件，下载地址：http://fir.im/9xj7");
                break;
        }
        return true;
    }

    /**
     * mAdapter
     */
    class MainAdapter extends FragmentStatePagerAdapter {

        public MainAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new HashMap<>();
            for (int i = 0; i < mCityList.size(); i++) {
                WeatherLazyFragment mFragment = new WeatherLazyFragment();
                Bundle mBundle = new Bundle();
                mBundle.putString("city", mCityList.get(i));
                mFragment.setArguments(mBundle);
                mFragments.put(mCityList.get(i), mFragment);
            }
        }

        //在已经有城市数据的基础上，添加城市调用该方法。
        public void addCity(String cityName) {
            WeatherLazyFragment fragment = new WeatherLazyFragment();
            Bundle mBundle = new Bundle();
            mBundle.putString("city", cityName);
            fragment.setArguments(mBundle);
            mFragments.put(cityName, fragment);
            notifyDataSetChanged();
            /**
             * 设置当前为最后一页，只能在notify之后才起作用。
             */
            mViewPager.setCurrentItem(mCityList.size() - 1);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(mCityList.get(position));
        }

        @Override
        public int getCount() {
            return mFragments == null ? 0 : mFragments.size();
        }

        /**
         * 由于ViewPager的预加载特性。
         * Fragment的视图会一致存在内存中，所有就会导致即使删除Fragment，还能弹出半的View，但是无法滑动
         * 过去的Bug。我们需要重写该方法，让其PagerAdapter.POSITION_NONE就可以测底删除Viewpager中的
         * Fragment。
         */
//        @Override
//        public int getItemPosition(Object object) {
//            return PagerAdapter.POSITION_NONE;
//        }
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (mDrawer.isDrawerOpen(GravityCompat.START)){
                mDrawer.closeDrawers();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
