package com.koterwong.weather.managercity;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koterwong.weather.R;
import com.koterwong.weather.base.BaseApplication;
import com.koterwong.weather.beans.WeatherBean;

import java.util.List;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ManagerCityActivity extends SwipeBackActivity implements ManagerCityView {

    /**
     * UI reference
     */
//    private SwipeBackLayout mSwipeBackLayout;
    private Toolbar mToolbar;
    private FloatingActionButton mFloatingAb;
    private RecyclerView mRecyclerView;
    private ManagerCityAdapter mAdapter;
    /**
     * data
     */
    private List<String> mCityDatas;

    private ManagerCityPresenter mPresenter;

    /**
     * 回传给MainActivity的数据
     */
    private Bundle mResultBundle;
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ManagerCityPresenter(this);
        initView();
        initRecyclerView();
        mResultBundle = new Bundle();
    }


    private void initView() {
        setContentView(R.layout.activity_manager_city);

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mFloatingAb = (FloatingActionButton) findViewById(R.id.fab);
        mFloatingAb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "滑动删除~", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_manager_city);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCityDatas = mPresenter.querySavedCityList();
        if (mCityDatas != null && mCityDatas.size() > 0) {
            mAdapter = new ManagerCityAdapter();
            mRecyclerView.setAdapter(mAdapter);
        }
        new ItemTouchHelper(new MySimpleCallBack()).attachToRecyclerView(mRecyclerView);
    }

    class MySimpleCallBack extends ItemTouchHelper.SimpleCallback {

        /**
         * Creates a Callback for the given drag and swipe allowance. These values serve as
         * defaults
         * and if you want to customize behavior per ViewHolder, you can override
         * {@link #getSwipeDirs(RecyclerView, RecyclerView.ViewHolder)}
         * and / or {@link #getDragDirs(RecyclerView, ViewHolder)}.
         *
         * @param dragDirs  Binary OR of direction flags in which the Views can be dragged. Must be
         *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
         *                  #END},
         *                  {@link #UP} and {@link #DOWN}.
         * @param swipeDirs Binary OR of direction flags in which the Views can be swiped. Must be
         *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
         *                  #END},
         *                  {@link #UP} and {@link #DOWN}.
         */
        /*
         * 1、dragDirs - 表示拖拽的方向，有六个类型的值：LEFT、RIGHT、START、END、UP、DOWN
         * 2、swipeDirs - 表示滑动的方向，有六个类型的值：LEFT、RIGHT、START、END、UP、DOWN
         * 3. 传入零表示不执行操作
         */
        public MySimpleCallBack() {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        /**
         * @param recyclerView
         * @param viewHolder   拖动的ViewHolder
         * @param target       目标位置的ViewHolder
         * @return
         */
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        /**
         * @param viewHolder 滑动的ViewHolder
         * @param direction  滑动的方向
         */
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            if (mCityDatas.size()==1){
                Snackbar.make(mRecyclerView,"请至少保留一个城市",Snackbar.LENGTH_LONG).show();
                mAdapter.notifyItemRemoved(position);
                return;
            }
            /*移除，并在数据库中删除城市*/
            String removeCity = mCityDatas.remove(position);
            mPresenter.deleteCity(removeCity);
            Snackbar.make(mRecyclerView,"删除成功~",Snackbar.LENGTH_LONG).show();
            /*将移除的数据保存到bundle中*/
            mResultBundle.putString(removeCity + position, removeCity);
            mAdapter.notifyItemRemoved(position);
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                //滑动时改变Item的透明度
                final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            }
        }
    }

    class ManagerCityAdapter extends RecyclerView.Adapter<ManagerCityHolder> {

        @Override
        public ManagerCityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ManagerCityHolder(LayoutInflater.from(BaseApplication.getApplication()).
                    inflate(R.layout.item_maneger_city, parent, false));
        }

        @Override
        public int getItemCount() {
            return mCityDatas == null ? 0 : mCityDatas.size();
        }

        @Override
        public void onBindViewHolder(ManagerCityHolder holder, int position) {
            holder.mTextView.setText(mCityDatas.get(position));
            WeatherBean.NowBean nowBean = mPresenter.querySimpleWeather(mCityDatas.get(position));
            if (nowBean!=null){
                holder.mTmpTV.setText(nowBean.tmp+"°");
                holder.mDesTv.setText(nowBean.cond.txt);
            }
        }
    }

    class ManagerCityHolder extends RecyclerView.ViewHolder {

        private TextView mTextView,mTmpTV,mDesTv;

        public ManagerCityHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_city);
            mTmpTV = (TextView) itemView.findViewById(R.id.tv_tmp);
            mDesTv = (TextView) itemView.findViewById(R.id.tv_des);
        }
    }

    /**
     * 回传数据，回传之前一定要调用finish()方发、
     */
    @Override
    public void onBackPressed() {
        Intent mIntent = new Intent();
        mIntent.putExtras(mResultBundle);
        setResult(RESULT_OK, mIntent);
        finish();
    }
}
