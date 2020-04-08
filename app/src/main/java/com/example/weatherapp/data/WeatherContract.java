package com.example.weatherapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class WeatherContract {

    public static final String CONTENT_AUTHORITY = "com.example.weatherapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FORECAST_WEATHER = "forecast_weather";

    public static final class ForecastWeatherEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FORECAST_WEATHER);

        //this is be name of table in database which will store forecast weather data
        public static final String TABLE_NAME = "forecast_weather";

        //this corresponds the name of column inside table forecast_weather
        //which stores the date of weather data corresponds to.
        public static final String COLUMN_DATE_TIME = "dt_txt";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIME = "time";

        //this corresponds the name of column inside table forecast_weather
        //which stores the time when the last weather data was entered in our forecast_weather table
        public static final String COLUMN_LAST_WEATHER_DATA_INSERTED_TIME = "last_updated";

        //temperature corresponding the specific date and time
        public static final String COLUMN_TEMP = "temp";
        public static final String COLUMN_TEMP_FEELS_LIKE = "feels_like";
        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_MAX_TEMP = "max";

        public static final String COLUMN_HUMIDITY = "humidity";


        public static final String COLUMN_PRESSURE = "pressure";
        public static final String COLUMN_PRESSURE_SEA_LEVEL = "sea_level";
        public static final String COLUMN_GROUND_LEVEL = "grnd_level";

        public static final String COLUMN_WIND_SPEED = "wind";

        public static final String COLUMN_DEGREES = "degrees";

        public static final String COLUMN_CLOUDS = "clouds";
        public static final String COLUMN_WEATHER_MAIN = "main";
        public static final String COLUMN_WEATHER_DESCRIPTION = "description";
        public static final String COLUMN_WEATHER_CONDITION_ICON = "icon";
    }

}
