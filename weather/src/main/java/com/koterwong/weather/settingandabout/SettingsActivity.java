package com.koterwong.weather.settingandabout;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.koterwong.weather.R;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;


public class SettingsActivity extends SwipeBackActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSwipeBackLayout().
                setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        setContentView(R.layout.activity_setting);
        init();
    }

    private void init() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("设置");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().replace(R.id.fl_content, new SettingsFragment()).commit();
    }
}
