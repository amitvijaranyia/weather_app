package com.example.weatherapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.preference.PreferenceManager;

import com.example.weatherapp.ForecastWeatherData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static List<String> getForecastWeatherListFromJson(Context context, String forecastJsonString)
            throws JSONException {
        if(TextUtils.isEmpty(forecastJsonString)){
            return null;
        }
        List<String> forecastWeatherDataList = new ArrayList<>();
        final String OWM_DT_TXT = "dt_txt";
        final String OWM_TEMP_MIN = "temp_min";
        final String OWM_TEMP_MAX = "temp_max";
        final String OWM_MAIN = "main";

        JSONObject forecastWeatherJson = new JSONObject(forecastJsonString);

        JSONArray list = forecastWeatherJson.getJSONArray("list");

        for(int i = 0; i < list.length(); i++){
            JSONObject currentWeather = list.getJSONObject(i);

            String dt_txt = currentWeather.getString(OWM_DT_TXT);
            String min_temp = currentWeather.getJSONObject("main").getString(OWM_TEMP_MIN);
            String max_temp = currentWeather.getJSONObject("main").getString(OWM_TEMP_MAX);
            String main = currentWeather.getJSONArray("weather")
                    .getJSONObject(0).getString(OWM_MAIN);

            String currentString = WeatherTimeUtils.formatDateAndTime(dt_txt)
                    + " - " + main + " - "
                    + WeatherOtherUtils.formatHighLowTemp(context, min_temp, max_temp);

            forecastWeatherDataList.add(currentString);
        }
        return forecastWeatherDataList;
    }

    public static List<ForecastWeatherData> getEntireForecastWeatherListFromJson(Context context, String forecastJsonString)
            throws JSONException{
        if(TextUtils.isEmpty(forecastJsonString)){
            return null;
        }
        List<ForecastWeatherData> forecastWeatherDataList = new ArrayList<>();
        final String OWM_DT_TXT = "dt_txt";
        final String OWM_TEMP = "temp";
        final String OWM_TEMP_FEELS_LIKE = "feels_like";
        final String OWM_TEMP_MIN = "temp_min";
        final String OWM_TEMP_MAX = "temp_max";
        final String OWM_PRESSURE = "pressure";
        final String OWM_PRESSURE_AT_SEA_LEVEL = "sea_level";
        final String OWM_PRESSURE_AT_GROUND_LEVEL = "grnd_level";
        final String OWM_HUMIDITY = "humidity";
        final String OWM_WIND_SPEED = "speed";
        final String OWM_WIND_DIRECTION = "deg";
        final String OWM_CLOUDS = "all";
        final String OWM_MAIN = "main";
        final String OWM_WEATHER_DESCRIPTION = "description";
        final String OWM_WEATHER_CONDITION_ICON = "icon";

        JSONObject forecastWeatherJson = new JSONObject(forecastJsonString);

        JSONArray list = forecastWeatherJson.getJSONArray("list");

        for(int i = 0; i < list.length(); i++){
            JSONObject currentWeather = list.getJSONObject(i);
            String date_time = currentWeather.getString(OWM_DT_TXT);
            String date = WeatherTimeUtils.formatDateAndTime(date_time);
            String time = WeatherTimeUtils.getTime(date_time);
            String lastUpdated = WeatherTimeUtils.getCurrentTime();
            double temp = currentWeather.getJSONObject("main").getDouble(OWM_TEMP);
            double feels_like = currentWeather.getJSONObject("main").getDouble(OWM_TEMP_FEELS_LIKE);
            double min_temp = currentWeather.getJSONObject("main").getDouble(OWM_TEMP_MIN);
            double max_temp = currentWeather.getJSONObject("main").getDouble(OWM_TEMP_MAX);
            double pressure = currentWeather.getJSONObject("main").getDouble(OWM_PRESSURE);
            double pressure_at_sea_level = currentWeather.getJSONObject("main").getDouble(OWM_PRESSURE_AT_SEA_LEVEL);
            double pressure_at_ground_level = currentWeather.getJSONObject("main").getDouble(OWM_PRESSURE_AT_GROUND_LEVEL);
            double humidity = currentWeather.getJSONObject("main").getDouble(OWM_HUMIDITY);
            double wind_speed = currentWeather.getJSONObject("wind").getDouble(OWM_WIND_SPEED);
            double wind_direction = currentWeather.getJSONObject("wind").getDouble(OWM_WIND_DIRECTION);
            double clouds = currentWeather.getJSONObject("clouds").getDouble(OWM_CLOUDS);
            String main = currentWeather.getJSONArray("weather").getJSONObject(0).getString(OWM_MAIN);
            String description = currentWeather.getJSONArray("weather").getJSONObject(0).getString(OWM_WEATHER_DESCRIPTION);
            String icon = currentWeather.getJSONArray("weather").getJSONObject(0).getString(OWM_WEATHER_CONDITION_ICON);

            ForecastWeatherData weatherData = new ForecastWeatherData(
                    date_time,
                    date,
                    time,
                    lastUpdated,
                    main,
                    description,
                    icon,
                    temp,
                    feels_like,
                    min_temp,
                    max_temp,
                    pressure,
                    pressure_at_sea_level,
                    pressure_at_ground_level,
                    humidity,
                    wind_speed,
                    wind_direction,
                    clouds
            );
            forecastWeatherDataList.add(weatherData);
        }
        return forecastWeatherDataList;
    }

    public static ForecastWeatherData getCurrentWeatherDataFromRawJSON(Context context, String currentWeatherJsonString)
            throws JSONException{
        if(TextUtils.isEmpty(currentWeatherJsonString)){
            return null;
        }
        final String OWM_TEMP = "temp";
        final String OWM_TEMP_FEELS_LIKE = "feels_like";
        final String OWM_TEMP_MIN = "temp_min";
        final String OWM_TEMP_MAX = "temp_max";
        final String OWM_PRESSURE = "pressure";
        final String OWM_HUMIDITY = "humidity";
        final String OWM_WIND_SPEED = "speed";
        final String OWM_WIND_DIRECTION = "deg";
        final String OWM_CLOUDS = "all";
        final String OWM_MAIN = "main";
        final String OWM_WEATHER_DESCRIPTION = "description";
        final String OWM_WEATHER_CONDITION_ICON = "icon";

        JSONObject forecastWeatherJson = new JSONObject(currentWeatherJsonString);

        String lastUpdated = WeatherTimeUtils.getCurrentTime();
        double temp = forecastWeatherJson.getJSONObject("main").getDouble(OWM_TEMP);
        double feels_like = forecastWeatherJson.getJSONObject("main").getDouble(OWM_TEMP_FEELS_LIKE);
        double min_temp = forecastWeatherJson.getJSONObject("main").getDouble(OWM_TEMP_MIN);
        double max_temp = forecastWeatherJson.getJSONObject("main").getDouble(OWM_TEMP_MAX);
        double pressure = forecastWeatherJson.getJSONObject("main").getDouble(OWM_PRESSURE);
        double humidity = forecastWeatherJson.getJSONObject("main").getDouble(OWM_HUMIDITY);
        double wind_speed = forecastWeatherJson.getJSONObject("wind").getDouble(OWM_WIND_SPEED);
        double wind_direction = forecastWeatherJson.getJSONObject("wind").getDouble(OWM_WIND_DIRECTION);
        double clouds = forecastWeatherJson.getJSONObject("clouds").getDouble(OWM_CLOUDS);
        String main = forecastWeatherJson.getJSONArray("weather").getJSONObject(0).getString(OWM_MAIN);
        String description = forecastWeatherJson.getJSONArray("weather").getJSONObject(0).getString(OWM_WEATHER_DESCRIPTION);
        String icon = forecastWeatherJson.getJSONArray("weather").getJSONObject(0).getString(OWM_WEATHER_CONDITION_ICON);
        String city_name = forecastWeatherJson.getString("name");
        boolean hasVisibility = forecastWeatherJson.has("visibility");
        int visibility = -1;
        if(hasVisibility) {
             visibility = forecastWeatherJson.getInt("visibility");
        }
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        preferences.edit().putString("city_name", city_name).commit();
        preferences.edit().putInt("visibility", visibility).commit();

        ForecastWeatherData weatherData = new ForecastWeatherData(
                "Amit",
                null,
                null,
                lastUpdated,
                main,
                description,
                icon,
                temp,
                feels_like,
                min_temp,
                max_temp,
                pressure,
                0,
                0,
                humidity,
                wind_speed,
                wind_direction,
                clouds
        );
        return weatherData;
    }

}
