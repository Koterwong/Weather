package com.koterwong.weather.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.koterwong.weather.R;
import com.koterwong.weather.commons.ActivityStatueBarCompat;
import com.koterwong.weather.utils.AppUtils;

import me.drakeet.materialdialog.MaterialDialog;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class AboutActivity extends SwipeBackActivity {


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initView();
        this.initFab();
    }

    private void initView() {
        setContentView(R.layout.activity_about);
        ActivityStatueBarCompat.compat(this);
        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_about);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((TextView)findViewById(R.id.tv_version)).setText("Version："+AppUtils.getVersionName(this));
    }

    private void initFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDialog dialog = new MaterialDialog(AboutActivity.this)
                        .setTitle("点赞")
                        .setMessage("去GitHub查看项目详细介绍，并给作者点个赞 o(∩_∩)o")
                        .setCanceledOnTouchOutside(true)
                        .setPositiveButton("好的", new View.OnClickListener() {
                            @Override public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.github.com/koterwong"));
                                startActivity(intent);
                            }
                        });
                dialog.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
