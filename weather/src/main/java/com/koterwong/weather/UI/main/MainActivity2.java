package com.koterwong.weather.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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

import com.koterwong.weather.R;
import com.koterwong.weather.commons.ActivityStatueBarCompat;
import com.koterwong.weather.ui.choicecity.ChoiceCityActivity;
import com.koterwong.weather.ui.main.presenter.MainPresenter;
import com.koterwong.weather.ui.main.presenter.MainPresenterImp;
import com.koterwong.weather.ui.main.view.MainView;
import com.koterwong.weather.utils.LogUtils;
import com.koterwong.weather.utils.RxBus;
import com.koterwong.weather.utils.ShareUtils;
import com.koterwong.weather.widget.ZoomOutSlideTransformer;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Author：Koterwong，Data：2016/4/27.
 * Description:
 * =================================
 * 1.删除Activity过度动画。因为在很多国内的Rom不起作用。
 */
public class MainActivity2 extends AppCompatActivity implements MainView, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String TAG = "MainActivity2";
    public static final int REQUEST_CODE_ADD = 0;
//    public static final int REQUEST_CODE_DELETE = 1;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;

    private MainPresenter mPresenter;
    private RelativeLayout mRlContent;
    private ViewPager mViewPager;
    private List<String> mCityList;
    private MainAdapter mAdapter;

    private CompositeSubscription mCompositeSubscription;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mCompositeSubscription = new CompositeSubscription();
        this.initView();
        this.initRxBus();
        this.mPresenter = new MainPresenterImp(this);
        this.mPresenter.loadCities();

    }

    private void initView() {
        setContentView(R.layout.activity_main2);
        ActivityStatueBarCompat.compat(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(mToolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
        NavigationView mNvView = (NavigationView) findViewById(R.id.nav_view);
        if (mNvView != null) {
            mNvView.setNavigationItemSelectedListener(this);
        }
        //没有城市界面。
        mRlContent = (RelativeLayout) findViewById(R.id.rl_content_choice);
        Button mChoiceBtn = (Button) findViewById(R.id.btn_choice_city);
        mChoiceBtn.setOnClickListener(this);
        //viewPager
        mViewPager = (ViewPager) findViewById(R.id.main_view_pager);
        //设置viewPager的缓冲数。2几位看不见的个数。
        mViewPager.setOffscreenPageLimit(2);
        //设置ViewPager动画
        mViewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
    }

    private void initRxBus() {
        Subscription subscribe = RxBus.getInstance()
                .toObervable()
                .map(new Func1<Object, ArrayList<String>>() {
                    @SuppressWarnings("unchecked")
                    @Override public ArrayList<String> call(Object o) {
                        if (o instanceof ArrayList) {
                            return (ArrayList<String>) o;
                        }
                        return null;
                    }
                })
                .subscribe(new Action1<ArrayList<String>>() {
                    @Override public void call(ArrayList<String> strings) {
                        for (String city:strings){
                            mAdapter.deleteCity(city);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
        mCompositeSubscription.add(subscribe);
    }

    /**
     * 加载城市数据成功时调用，没有城市数据不会调用该方法。
     * 只会调用一次,即在程序第一次请求城市的时候掉用。
     *
     * @param mCityList 城市信息
     */
    @Override public void setCities(List<String> mCityList) {
        this.mCityList = mCityList;
        if (mAdapter == null) {
            mAdapter = new MainAdapter(getSupportFragmentManager(), this.mCityList);
            mViewPager.setAdapter(mAdapter);
        }
    }

    @Override public void addCity(String cityName) {
        if (mAdapter == null) {
            this.mCityList = new ArrayList<>();
            this.mCityList.add(cityName);
            this.mAdapter = new MainAdapter(getSupportFragmentManager(), this.mCityList);
            this.setContentVisible(true);
            this.mViewPager.setAdapter(mAdapter);
        } else {
            //额外添加了城市
            this.mAdapter.addCity(cityName);
            this.mViewPager.setCurrentItem(this.mCityList.size() - 1);
        }
    }

    @Override public void setToolbarTitle(String title) {
        mToolbar.setTitle(title);
    }

    @Override public void setContentVisible(boolean visible) {
        mViewPager.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        mRlContent.setVisibility(visible ? View.INVISIBLE : View.VISIBLE);
    }

    @Override public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        mDrawer.closeDrawer(GravityCompat.START);
        mPresenter.switchNavigation(item.getItemId());
        return true;
    }

    @Override public void switch2Activity(Class activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    @Override public void switch2ActivityForResult(Class activityClass, int reqCode) {
        Intent intent = new Intent(this, activityClass);
        startActivityForResult(intent, reqCode);
    }

    @Override public void onClick(View v) {
        this.switch2ActivityForResult(ChoiceCityActivity.class, REQUEST_CODE_ADD);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ADD:
                if (resultCode == RESULT_OK) {
                    String city = data.getStringExtra("city");
                    if (city == null) {
                        Snackbar.make(mRlContent, "添加失败", Snackbar.LENGTH_LONG);
                        return;
                    }
                    this.addCity(city);
                    mPresenter.addCityToDB(city);
                }
                break;
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                    mDrawer.closeDrawers();
                } else {
                    mDrawer.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.action_share:
                ShareUtils.share(MainActivity2.this, getString(R.string.share_app));
                break;
        }
        return true;
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.unsubscribe();
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                mDrawer.closeDrawers();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
