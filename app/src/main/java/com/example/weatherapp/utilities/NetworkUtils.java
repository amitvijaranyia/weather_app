package com.example.weatherapp.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.weatherapp.data.WeatherPreferences;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {

    //TAG for logs
    private static final String TAG = "tag";

    private static final String FORECAST_BASE_URL =
            "https://api.openweathermap.org/data/2.5/forecast?";

    private static final String CURRENT_BASE_URL =
            "httpS://api.openweathermap.org/data/2.5/weather?";

    //query parameter for city name
    private static final String QUERY_PARAM = "q";
    //parameter for zip code
    private static final String ZIP_PARAM = "zip";
    //parameter for latitude of the location
    private static final String LATITUDE_PARAM = "lat";
    //parameter for longitude of the location
    private static final String LONGITUDE_PARAM = "lon";
    //parameter for return format of response(this will always be JSON)
    private static final String MODE_PARAM = "mode";
    //parameter for units of temp and wind speed
    private static final String UNITS_PARAM = "units";
    //token id
    private static final String TOKEN_KEY_PARAM = "APPID";
    //parameter for language
    private static final String LANG_PARAM = "lang";

    private static final String mode = "json";
    private static final String appid = "15bb503402768b6d8b52cc9e7215f754";

    public static URL buildUrl(Context context, String locationQuery){
        Uri uri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, locationQuery)
                .appendQueryParameter(MODE_PARAM, mode)
                .appendQueryParameter(UNITS_PARAM, WeatherPreferences.getUnits(context))
                .appendQueryParameter(TOKEN_KEY_PARAM, appid)
                .appendQueryParameter(LANG_PARAM, WeatherPreferences.getPreferredLanguage(context))
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildUrl(Context context, double latitude, double longitude) {
        Uri uri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(LATITUDE_PARAM, String.valueOf(latitude))
                .appendQueryParameter(LONGITUDE_PARAM, String.valueOf(longitude))
                .appendQueryParameter(MODE_PARAM, mode)
                .appendQueryParameter(UNITS_PARAM, WeatherPreferences.getUnits(context))
                .appendQueryParameter(TOKEN_KEY_PARAM, appid)
                .appendQueryParameter(LANG_PARAM, WeatherPreferences.getPreferredLanguage(context))
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildForecastUrlWithZIP(Context context, String zipCode){
        Uri uri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(ZIP_PARAM, zipCode)
                .appendQueryParameter(MODE_PARAM, mode)
                .appendQueryParameter(UNITS_PARAM, WeatherPreferences.getUnits(context))
                .appendQueryParameter(LANG_PARAM, WeatherPreferences.getPreferredLanguage(context))
                .appendQueryParameter(TOKEN_KEY_PARAM, appid)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildCurrentUrlWithZip(Context context, String zipCode){
        Uri uri = Uri.parse(CURRENT_BASE_URL).buildUpon()
                .appendQueryParameter(ZIP_PARAM, zipCode)
                .appendQueryParameter(MODE_PARAM, mode)
                .appendQueryParameter(UNITS_PARAM, WeatherPreferences.getUnits(context))
                .appendQueryParameter(LANG_PARAM, WeatherPreferences.getPreferredLanguage(context))
                .appendQueryParameter(TOKEN_KEY_PARAM, appid)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
