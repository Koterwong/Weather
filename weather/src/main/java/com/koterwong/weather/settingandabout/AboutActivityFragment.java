package com.koterwong.weather.settingandabout;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.koterwong.weather.R;

/**
 *
 */
public class AboutActivityFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_about);
    }
}
