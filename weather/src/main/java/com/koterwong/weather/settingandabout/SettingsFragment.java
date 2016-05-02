package com.koterwong.weather.settingandabout;


import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.view.View;

import com.koterwong.weather.R;
import com.koterwong.weather.commons.Setting;

public class SettingsFragment extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener,
        Preference.OnPreferenceClickListener {

    private final String SWITCH_LOCATION = "switch_location";
    private final String SWITCH_UPDATE = "switch_update";
    private final String SYNC_FREQUENCY = "sync_frequency";
    private final String CB_NOTIFICATION = "cb_notification";
    private final String CB_NOTIFICATION_ICON = "cb_notification_icon";

    private SwitchPreference mSwitchLocation;
    private SwitchPreference mSwitchUpdate;
    private ListPreference mSyncFrequency;
    private CheckBoxPreference mCbNotification;
    private CheckBoxPreference mCbNotificationIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting);

        mSwitchLocation = (SwitchPreference) findPreference(SWITCH_LOCATION);
        mSwitchUpdate = (SwitchPreference) findPreference(SWITCH_UPDATE);
        mSyncFrequency = (ListPreference) findPreference(SYNC_FREQUENCY);
        mCbNotification = (CheckBoxPreference) findPreference(CB_NOTIFICATION);
        mCbNotificationIcon = (CheckBoxPreference) findPreference(CB_NOTIFICATION_ICON);

        mSwitchLocation.setOnPreferenceChangeListener(this);
        mSwitchUpdate.setOnPreferenceChangeListener(this);
        mSyncFrequency.setOnPreferenceChangeListener(this);
        mCbNotification.setOnPreferenceChangeListener(this);
        mCbNotificationIcon.setOnPreferenceChangeListener(this);

        mSyncFrequency.setSummary(mSyncFrequency.getEntry());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (preference instanceof SwitchPreference) {
            boolean statue = Boolean.valueOf(newValue.toString());
            if (preference.getKey().equals(SWITCH_LOCATION)) {
                /*将时候允许定位信息保存 */
//                L.e(preference.getKey(), statue + "");
                Setting.putBoolean(Setting.IS_ALLOW_LOCATION, statue);
            } else if (preference.getKey().equals(SWITCH_UPDATE)) {
//                L.e(preference.getKey(), statue + "");
                //后台更新
                Setting.putBoolean(Setting.IS_ALLOW_UPDATE, statue);
            }
        } else if (preference instanceof CheckBoxPreference) {
            if (preference.getKey().equals(CB_NOTIFICATION)) {
//                L.e(preference.getKey(), newValue.toString());
                //打开或取消通知
                switchNotification(Boolean.valueOf(newValue.toString()));

            } else if (preference.getKey().equals(CB_NOTIFICATION_ICON)) {
//                L.e(preference.getKey(), newValue.toString());
                //显示隐藏Notification的Icon
                switchNotificationIcon(Boolean.valueOf(newValue.toString()));
            }
        } else if (preference instanceof ListPreference) {
            if (preference.getKey().equals(SYNC_FREQUENCY)) {
//                L.e(preference.getKey(), newValue.toString());
                //后台更新的间隔
                Setting.putString(Setting.AUTO_UPDATE_TIME, newValue.toString());
                mSyncFrequency.setSummary(newValue.toString() + " 小时");
            }
        }
        return true;
    }

    /**/
    private void switchNotification(boolean statue) {
        Intent intent  =  new Intent("com.koterwong.weather.Notification");
        intent.putExtra("isShow",statue);
        getActivity().sendBroadcast(intent);
    }

    private void switchNotificationIcon(boolean statue) {

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        return true;
    }
}
