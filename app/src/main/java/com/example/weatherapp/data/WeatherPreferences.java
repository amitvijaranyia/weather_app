package com.example.weatherapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

public class WeatherPreferences {

    private static final String TAG = "tag";

    private static final String DEFAULT_WEATHER_LOCATION_ZIP_CODE = "110005,in";

    public static String getPreferredWeatherLocation(Context context) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String location_zip = preferences.getString("location_zip", "");
        String pref_country_name = preferences.getString("pref_country_name", "");

        String preferredWeatherLocation = location_zip + "," + pref_country_name;
        if(preferredWeatherLocation.length() != 1){
            return preferredWeatherLocation;
        }
        else {
            return getDefaultWeatherLocation();
        }
    }

    public static String  getUnits(Context context) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String units = preferences.getString("units_preference", "metric");
        return units;
    }

    public static String getPreferredLanguage(Context context){
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String preferredLanguage = preferences.getString("pref_weather_data_language", "");
        return preferredLanguage;
    }

    private static String getDefaultWeatherLocation() {
        return DEFAULT_WEATHER_LOCATION_ZIP_CODE;
    }
}
