package com.koterwong.weather.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koterwong.weather.R;
import com.koterwong.weather.beans.VersionBean;
import com.koterwong.weather.commons.Setting;
import com.koterwong.weather.utils.AppUtils;
import com.koterwong.weather.utils.DateUtils;

import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;
import me.drakeet.materialdialog.MaterialDialog;

/**
 *
 */
public class AboutFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    private Preference version;
    private Preference update;
    private Preference weather_data;
    private Preference location;
    private Preference pic;
    private Preference weibo;
    private Preference dependencies;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_about);

        version = findPreference("version");
        update = findPreference("update");
        weather_data = findPreference("weather_data");
        location = findPreference("location");
        pic = findPreference("pic");
        weibo = findPreference("weibo");
        dependencies = findPreference("dependencies");

        version.setOnPreferenceClickListener(this);
        update.setOnPreferenceClickListener(this);
        weather_data.setOnPreferenceClickListener(this);
        pic.setOnPreferenceClickListener(this);
        location.setOnPreferenceClickListener(this);
        weibo.setOnPreferenceClickListener(this);
        dependencies.setOnPreferenceClickListener(this);
        version.setSummary(String.format("%s %s",
                AppUtils.getAppName(getActivity()),
                AppUtils.getVersionName(getActivity())));
        update.setSummary(Setting.getBoolean(Setting.IS_HAS_NEW_VERSION, false) ?
                getResources().getString(R.string.has_new_version) :
                getResources().getString(R.string.this_is_new_version));
    }

    @Override public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "version":
                showDialog(version.getTitle(), version.getSummary());
                break;
            case "update":
                checkVersion();
                break;
            case "weather_data":
                showDialog(weather_data.getTitle(), weather_data.getSummary());
                break;
            case "pic":
                showDialog(pic.getTitle(), pic.getSummary());
                break;
            case "location":
                showDialog(location.getTitle(), location.getSummary());
                break;
            case "weibo":
                showDialog(weibo.getTitle(), getString(R.string.contact_me));
                break;
            case "dependencies":
                showDialog(dependencies.getTitle(), dependencies.getSummary());
                break;
        }
        return true;
    }

    private void showDialog(CharSequence title, CharSequence summary) {
        MaterialDialog dialog = new MaterialDialog(getActivity())
                .setTitle(title)
                .setMessage(summary)
                .setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private static final String firToken = "75385e2fdcc392ee6c31e06620cdf6ef";
    private boolean isChecking = false;

    public void checkVersion() {

        if (isChecking) {
            Snackbar.make(getView(), "正在检查更新，请不要重复点击", Snackbar.LENGTH_SHORT).show();
            return;
        }
        isChecking = true;
        FIR.checkForUpdateInFIR(firToken, new VersionCheckCallback() {
            @Override public void onSuccess(String s) {
                super.onSuccess(s);
                Gson mGons = new GsonBuilder()
                        .create();
                VersionBean versionBean = mGons.fromJson(s, VersionBean.class);
                int versionCode = AppUtils.getVersionCode(getActivity());
                if (versionCode < versionBean.version) {
                    showUpdateDialog(versionBean);
                    Setting.putBoolean(Setting.IS_HAS_NEW_VERSION, true);
                } else {
                    Snackbar.make(getView(), "当前为最新版本", Snackbar.LENGTH_SHORT).show();
                    Setting.putBoolean(Setting.IS_HAS_NEW_VERSION, false);
                }
            }

            @Override public void onFail(Exception e) {
                super.onFail(e);
                Snackbar.make(getView(), "检查失败，请检查网络", Snackbar.LENGTH_SHORT).show();
            }

            @Override public void onStart() {
                super.onStart();
                Snackbar.make(getView(), "正在检查更新", Snackbar.LENGTH_SHORT).show();
            }

            @Override public void onFinish() {
                super.onFinish();
                isChecking = false;
            }
        });
    }

    private void showUpdateDialog(final VersionBean versionBean) {
        final MaterialDialog dialog = new MaterialDialog(getActivity())
                .setTitle("新版本")
                .setMessage(
                        "更新时间：" + DateUtils.dateByString(versionBean.updated_at) + "\n" +
                                versionBean.changelog)
                .setCanceledOnTouchOutside(true)
                .setPositiveButton("更新", new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(versionBean.update_url));
                        startActivity(intent);
                    }
                });
        dialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
