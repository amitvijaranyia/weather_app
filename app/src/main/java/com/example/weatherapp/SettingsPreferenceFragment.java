package com.example.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsPreferenceFragment
        extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = "tag";
    
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_settings, rootKey);
//        setIconToNotification("notifications");
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        if(key.equalsIgnoreCase("notifications")) {
//            setIconToNotification(key);
//        }
    }

    public void setIconToNotification(String key){
        Preference preference = findPreference(key);
        SharedPreferences sharedPreference =PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        if(preference instanceof SwitchPreferenceCompat){
            if(sharedPreference.getBoolean(key, true)) {
                preference.setIcon(R.drawable.notifications_active_icon);
                preference.setTitle(R.string.weather_notifications_enabled);
            }
            else {
                preference.setIcon(R.drawable.notifications_off_icon);
                preference.setTitle(R.string.weather_notifications_disabled);
            }
        }
    }
}
