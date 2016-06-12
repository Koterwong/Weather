package com.koterwong.weather.ui.choicecity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.koterwong.weather.R;
import com.koterwong.weather.beans.CityBean;
import com.koterwong.weather.beans.ProvinceBean;
import com.koterwong.weather.commons.ActivityStatueBarCompat;
import com.koterwong.weather.ui.choicecity.View.CityView;
import com.koterwong.weather.ui.choicecity.presenter.CityPresenter;
import com.koterwong.weather.ui.choicecity.presenter.CityPresenterImp;
import com.koterwong.weather.commons.database.SavedCityDBManager;
import com.koterwong.weather.widget.BorderDividerItemDecoration;
import com.koterwong.weather.utils.ToolsUtil;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ChoiceCityActivity extends SwipeBackActivity implements CityView {

    //ui
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    private CityPresenter mCityPresenter;
    private CityListAdapter mAdapter;
    private CollapsingToolbarLayout mCollapsing;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCityPresenter = new CityPresenterImp(this);
        this.initView();
        this.initEvent();
    }

    private void initView() {
        setContentView(R.layout.activity_choice_city);
        ActivityStatueBarCompat.compat(this);
        SwipeBackLayout mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        //ScrollToolbar
        mCollapsing = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsing.setTitle("选择城市");
        //back
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_city);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CityListAdapter();
        mRecyclerView.addItemDecoration(new BorderDividerItemDecoration(
                getResources().getDimensionPixelOffset(R.dimen.item_ver),
                getResources().getDimensionPixelOffset(R.dimen.item_hor)
        ));
        SlideInBottomAnimationAdapter adapter = new SlideInBottomAnimationAdapter(mAdapter);
        adapter.setFirstOnly(false);
        mRecyclerView.setAdapter(adapter);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mCityPresenter.queryProvince();
    }

    private void initEvent() {
        mAdapter.setOnItemClickListener(new CityListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View itemView, int position, String msg ,String id, int level) {
                switch (level) {
                    case CityListAdapter.LEVEL_PROVINCE:
                        mCityPresenter.queryCity(id);
                        mCollapsing.setTitle(msg);
                        break;
                    case CityListAdapter.LEVEL_CITY:
                       // 判断保存城市的数据中是否存在该城市
                        boolean containTheCity = SavedCityDBManager.getInstance(ChoiceCityActivity.this).isExistCity(msg);
                        if (containTheCity){
                            Snackbar.make(mRecyclerView,"已经包含该城市",Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        if (!ToolsUtil.isNetworkAvailable(ChoiceCityActivity.this)){
                            Snackbar.make(mRecyclerView,"网络未连接，请连接网络重试",Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        Intent intent = new Intent();
                        intent.putExtra("city",msg);
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void setProDatas(List<ProvinceBean> mDatas) {
        mAdapter.setmProDatas(mDatas);
        setTitle("选择城市");
    }

    @Override public void setCityDatas(List<CityBean> mDatas) {
        mAdapter.setmCityDatass(mDatas);
    }

    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override public void setTitle(String title) {
        mCollapsing.setTitle(title);
    }

    @Override public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override public void onBackPressed() {
        if (CityListAdapter.currentLevel == CityListAdapter.LEVEL_CITY){
            //回到省份
            mCityPresenter.queryProvince();
        }else{
            super.onBackPressed();
        }
    }
}
