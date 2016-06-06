package com.koterwong.weather.settingandabout;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.koterwong.weather.R;

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
    }

    @Override public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "version":
                showDialog(version.getTitle(),version.getSummary());
                break;
            case "update":
                showDialog(update.getTitle(),update.getSummary());
                break;
            case "weather_data":
                showDialog(weather_data.getTitle(),weather_data.getSummary());
                break;
            case "pic":
                showDialog(pic.getTitle(),pic.getSummary());
                break;
            case "location":
                showDialog(location.getTitle(),location.getSummary());
                break;
            case "weibo":
                showDialog(weibo.getTitle(),weibo.getSummary());
                break;
            case "dependencies":
                showDialog(dependencies.getTitle(),dependencies.getSummary());
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
}
