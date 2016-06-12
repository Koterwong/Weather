package com.koterwong.weather.ui.setting;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.koterwong.weather.MyApp;
import com.koterwong.weather.R;
import com.koterwong.weather.beans.VersionBean;
import com.koterwong.weather.commons.SettingPref;
import com.koterwong.weather.receiver.LocalRegisterHelper;
import com.koterwong.weather.receiver.NotificationReceiver;
import com.koterwong.weather.service.AutoUpdateService;
import com.koterwong.weather.utils.AppUtils;
import com.koterwong.weather.utils.DateUtils;
import com.koterwong.weather.utils.FileUtils;
import com.koterwong.weather.utils.JsonUtils;

import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;
import me.drakeet.materialdialog.MaterialDialog;

public class SettingsFragment extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener,
        Preference.OnPreferenceClickListener {

    private final String UPDATE = "update";
    private final String SWITCH_LOCATION = "switch_location";
    private final String SWITCH_UPDATE = "switch_update";
    private final String SYNC_FREQUENCY = "sync_frequency";

    private final String CB_NOTIFICATION = "cb_notification";

    private final String LIST_PRE_CACHE_TIME = "list_pre_cache_time";
    private final String PREF_CACHE_SIZE_CLEAR = "pref_cache_size_clear";

    private Preference mUpdate;
    private SwitchPreference mSwitchLocation;
    private SwitchPreference mSwitchUpdate;
    private ListPreference mSyncFrequency;
    private CheckBoxPreference mCbNotification;

    private ListPreference mCacheSizeListPref;
    private Preference mCacheSizePref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting);

        mUpdate = findPreference(UPDATE);
        mSwitchLocation = (SwitchPreference) findPreference(SWITCH_LOCATION);
        mSwitchUpdate = (SwitchPreference) findPreference(SWITCH_UPDATE);
        mSyncFrequency = (ListPreference) findPreference(SYNC_FREQUENCY);
        mCbNotification = (CheckBoxPreference) findPreference(CB_NOTIFICATION);
        mCacheSizeListPref = (ListPreference) findPreference(LIST_PRE_CACHE_TIME);
        mCacheSizePref = findPreference(PREF_CACHE_SIZE_CLEAR);

        mUpdate.setOnPreferenceClickListener(this);
        mSwitchLocation.setOnPreferenceChangeListener(this);
        mSwitchUpdate.setOnPreferenceChangeListener(this);
        mSyncFrequency.setOnPreferenceChangeListener(this);
        mCbNotification.setOnPreferenceChangeListener(this);
        mCacheSizeListPref.setOnPreferenceChangeListener(this);
        mCacheSizePref.setOnPreferenceClickListener(this);

        //设置子标题为当前数据的实体
        mSyncFrequency.setSummary(mSyncFrequency.getEntry());
        mCacheSizeListPref.setSummary(mCacheSizeListPref.getEntry());

        //设置缓冲大小
        mCacheSizePref.setSummary(FileUtils.getCacheSize());
        //设置是否允许定位的开关。
        mSwitchLocation.setChecked(SettingPref.getBoolean(SettingPref.IS_ALLOW_LOCATION, false));

        mUpdate.setSummary(SettingPref.getBoolean(SettingPref.IS_HAS_NEW_VERSION, false) ?
                getResources().getString(R.string.has_new_version) :
                getResources().getString(R.string.this_is_new_version));
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference instanceof SwitchPreference) {
            boolean statue = Boolean.valueOf(newValue.toString());
            if (preference.getKey().equals(SWITCH_LOCATION)) {
                //将是否允许定位信息保存
                SettingPref.putBoolean(SettingPref.IS_ALLOW_LOCATION, statue);
            } else if (preference.getKey().equals(SWITCH_UPDATE)) {
                //后台更新
                SettingPref.putBoolean(SettingPref.IS_ALLOW_UPDATE, statue);
                switchAutoUpdateService(Boolean.valueOf(newValue.toString()));

            }
        } else if (preference instanceof CheckBoxPreference) {
            if (preference.getKey().equals(CB_NOTIFICATION)) {
                //打开或取消通知栏
                SettingPref.putBoolean(SettingPref.IS_SHOW_NOTIFY, Boolean.valueOf(newValue.toString()));
                switchNotification();
            }
        } else if (preference instanceof ListPreference) {
            if (preference.getKey().equals(SYNC_FREQUENCY)) {
                //后台更新的间隔
                SettingPref.putString(SettingPref.AUTO_UPDATE_TIME, newValue.toString());
                mSyncFrequency.setSummary(newValue.toString() + " 小时");
            } else if (preference.getKey().equals(LIST_PRE_CACHE_TIME)) {
                SettingPref.putString(SettingPref.AUTO_DELETE_CACHE_TIME, newValue.toString());
                mCacheSizeListPref.setSummary(newValue.toString() + " 小时");
            }
        }
        return true;
    }

    @Override public boolean onPreferenceClick(Preference preference) {
        if (preference == mCacheSizePref) {
            this.showDialog("清理缓存", getActivity().getString(R.string.clear_cache));
        } else if (preference == mUpdate) {
            this.checkVersion();
        }
        return true;
    }

    private void showDialog(CharSequence title, CharSequence summary) {
        final MaterialDialog dialog = new MaterialDialog(getActivity())
                .setTitle(title)
                .setMessage(summary)
                .setCanceledOnTouchOutside(true);
        dialog.setPositiveButton("OK", new View.OnClickListener() {
            @Override public void onClick(View v) {
                MyApp.getACache().clear();
                mCacheSizePref.setSummary(FileUtils.getCacheSize());
                Snackbar.make(getView(), "缓存已清除", Snackbar.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void switchAutoUpdateService(boolean statue) {
        //打开或关闭服务
        Intent intent = new Intent(getActivity(), AutoUpdateService.class);
        if (statue) {
            //启动服务
            getActivity().startService(intent);
        } else {
            //关闭服务
            getActivity().stopService(intent);
        }
    }

    private static final String firToken = "75385e2fdcc392ee6c31e06620cdf6ef";
    private boolean isChecking = false;

    private void checkVersion() {
        if (isChecking) {
            Snackbar.make(getView(), "正在检查更新，请不要重复点击", Snackbar.LENGTH_SHORT).show();
            return;
        }
        isChecking = true;
        FIR.checkForUpdateInFIR(firToken, new VersionCheckCallback() {
            @Override public void onSuccess(String s) {
                super.onSuccess(s);
                VersionBean versionBean = JsonUtils.deserialize(s, VersionBean.class);
                int versionCode = AppUtils.getVersionCode(getActivity());
                if (versionCode < versionBean.version) {
                    mUpdate.setSummary(getString(R.string.has_new_version));
                    showUpdateDialog(versionBean);
                    SettingPref.putBoolean(SettingPref.IS_HAS_NEW_VERSION, true);
                } else {
                    mUpdate.setSummary(getString(R.string.this_is_new_version));
                    Snackbar.make(getView(), "当前为最新版本", Snackbar.LENGTH_SHORT).show();
                    SettingPref.putBoolean(SettingPref.IS_HAS_NEW_VERSION, false);
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

    private NotificationReceiver receiver;

    private void switchNotification() {
        //发送广播，为了安全性考虑，只发送本地广播,其他程序则无法收到广播
        receiver = new NotificationReceiver();
        LocalRegisterHelper.getInstance(getActivity()).send(receiver, LocalRegisterHelper.action_notify);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            LocalRegisterHelper.getInstance(getActivity()).unRegister(receiver);
        }
    }
}
