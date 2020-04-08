package com.example.weatherapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.weatherapp.data.WeatherContract.ForecastWeatherEntry;

import androidx.annotation.Nullable;

public class WeatherDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "weather.db";

    private static final int DATABASE_VERSION = 4;

    public WeatherDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FORECAST_WEATHER_TABLE =
                "CREATE TABLE " + ForecastWeatherEntry.TABLE_NAME +
                  " (" +
                        ForecastWeatherEntry._ID                                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "               +
                        ForecastWeatherEntry.COLUMN_DATE_TIME                       + " TEXT, "                                    +
                        ForecastWeatherEntry.COLUMN_DATE                            + " TEXT, "                                   +
                        ForecastWeatherEntry.COLUMN_TIME                            + " TEXT, "                                   +
                        ForecastWeatherEntry.COLUMN_LAST_WEATHER_DATA_INSERTED_TIME + " TEXT NOT NULL,"                                    +
                        ForecastWeatherEntry.COLUMN_TEMP                            + " REAL NOT NULL, "                                   +
                        ForecastWeatherEntry.COLUMN_TEMP_FEELS_LIKE                 + " REAL NOT NULL, "                                   +
                        ForecastWeatherEntry.COLUMN_MIN_TEMP                        + " REAL NOT NULL, "                                   +
                        ForecastWeatherEntry.COLUMN_MAX_TEMP                        + " REAL NOT NULL, "                                   +
                        ForecastWeatherEntry.COLUMN_PRESSURE                        + " REAL NOT NULL, "                                   +
                        ForecastWeatherEntry.COLUMN_PRESSURE_SEA_LEVEL              + " REAL NOT NULL, "                                   +
                        ForecastWeatherEntry.COLUMN_GROUND_LEVEL                    + " REAL NOT NULL, "                                   +
                        ForecastWeatherEntry.COLUMN_HUMIDITY                        + " REAL NOT NULL, "                                   +
                        ForecastWeatherEntry.COLUMN_WIND_SPEED                      + " REAL NOT NULL, "                                   +
                        ForecastWeatherEntry.COLUMN_DEGREES                         + " REAL NOT NULL, "                                   +
                        ForecastWeatherEntry.COLUMN_CLOUDS                          + " REAL NOT NULL, "                                   +
                        ForecastWeatherEntry.COLUMN_WEATHER_MAIN                    + " TEXT NOT NULL, "                                   +
                        ForecastWeatherEntry.COLUMN_WEATHER_DESCRIPTION             + " TEXT NOT NULL, "                                   +
                        ForecastWeatherEntry.COLUMN_WEATHER_CONDITION_ICON          + " TEXT NOT NULL"                                     +
                 ");";
        db.execSQL(SQL_CREATE_FORECAST_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ForecastWeatherEntry.TABLE_NAME);
        onCreate(db);
    }
}
