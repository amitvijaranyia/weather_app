package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.app.LoaderManager.*;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.example.weatherapp.data.WeatherContract;
import com.example.weatherapp.data.WeatherPreferences;
import com.example.weatherapp.utilities.JsonUtils;
import com.example.weatherapp.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InsertDataIntoDatabase {

    private static final String TAG = "tag";

    private static  Context mContext;

    private static LoaderManager mLoaderManager;

    private static int LOADER_ID = 5;

    public static void insertIntoDatabase(Context context, LoaderManager manager){
        mContext = context;
        mLoaderManager = manager;
        mLoaderManager.initLoader(LOADER_ID, null, fetchForecastWeatherDataListener);
    }

    public static void restartLoader(Context context, LoaderManager manager){
        mContext = context;
        mLoaderManager = manager;

        manager.restartLoader(LOADER_ID, null, fetchForecastWeatherDataListener);
    }

    private static LoaderCallbacks<List<ForecastWeatherData>> fetchForecastWeatherDataListener =
            new LoaderCallbacks<List<ForecastWeatherData>>() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public Loader<List<ForecastWeatherData>> onCreateLoader(int id, Bundle args) {
                    return new AsyncTaskLoader<List<ForecastWeatherData>>(mContext) {
                        List<ForecastWeatherData> mForecastWeatherList = null;

                        @Override
                        protected void onStartLoading() {
//                            Log.d(TAG, "onStartLoading: " + "inside on start loading");
                            super.onStartLoading();
                            if(mForecastWeatherList != null){
                                deliverResult(mForecastWeatherList);
                            }
                            else{
//                                Log.d(TAG, "onStartLoading: " + "about to execute force load");
                                forceLoad();
                            }
                        }

                        @Override
                        public List<ForecastWeatherData> loadInBackground() {
//                            Log.d(TAG, "loadInBackground: " + "inside on load in background");
                            List<ForecastWeatherData> weatherDataList = new ArrayList<>();

                            String zip_code = WeatherPreferences
                                    .getPreferredWeatherLocation(mContext);

                            //here current weather data will be fetched
                            URL currentWeatherUrl = NetworkUtils.buildCurrentUrlWithZip(mContext, zip_code);
                            OkHttpClient currentWeatherClient = new OkHttpClient();
                            Request currentWeatherRequest = new Request.Builder()
                                    .url(currentWeatherUrl)
                                    .build();
                            try{
                                Response response = currentWeatherClient.newCall(currentWeatherRequest).execute();
                                String currentWeatherJson = response.body().string();
                                ForecastWeatherData currentWeatherData = JsonUtils
                                        .getCurrentWeatherDataFromRawJSON(mContext, currentWeatherJson);
                                weatherDataList.add(currentWeatherData);
                            }catch (IOException e) {
                                e.printStackTrace();
                                return null;
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return null;
                            }


                            //here forecast weather data will be fetched
                            URL forecastWeatherDataUrl = NetworkUtils.buildForecastUrlWithZIP(mContext, zip_code);
                            OkHttpClient forecastWeatherDataClient = new OkHttpClient();
                            Request forecastWeatherDataRequest = new Request.Builder()
                                    .url(forecastWeatherDataUrl)
                                    .build();


                            try {
                                Response response = forecastWeatherDataClient.newCall(forecastWeatherDataRequest).execute();
                                String forecastWeatherJson = response.body().string();
                                List<ForecastWeatherData> forecastWeatherDataList = JsonUtils
                                        .getEntireForecastWeatherListFromJson(mContext, forecastWeatherJson);
//                                Log.d(TAG, "loadInBackground: " + forecastWeatherDataList.size());
                                weatherDataList.addAll(forecastWeatherDataList);
                                return weatherDataList;
                            } catch (IOException e) {
                                e.printStackTrace();
                                return null;
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }

                        @Override
                        public void deliverResult(List<ForecastWeatherData> data) {
//                            Log.d(TAG, "deliverResult: " + "inside deliver result");
                            mForecastWeatherList = data;
                            super.deliverResult(data);
                        }
                    };
                }

                @Override
                public void onLoadFinished(Loader<List<ForecastWeatherData>> loader, List<ForecastWeatherData> data) {
//                    Log.d(TAG, "onLoadFinished: " + "inside on load finished");
                    if(data != null && data.size() > 0){
                        int rowDeleted = mContext.getContentResolver().delete(
                                WeatherContract.ForecastWeatherEntry.CONTENT_URI,
                                null,
                                null
                        );
                        Log.d(TAG, "onLoadFinished: " + "num of rows deleted = " + rowDeleted);
                        bulkInsertForecastWeatherData(data);
//                        Log.d(TAG, "onLoadFinished: " + data.size());
                        SharedPreferences preferences = PreferenceManager
                                .getDefaultSharedPreferences(mContext);
                        preferences.edit().putString("loading", "success").commit();
                    }
                    else{
                        Log.d(TAG, "onLoadFinished: " + " about to change loading value");
                        SharedPreferences preferences = PreferenceManager
                                .getDefaultSharedPreferences(mContext);
                        preferences.edit().putString("loading", "failed").commit();
                        String valueThatJustPutInLoading = preferences.getString("loading", "");
                        Log.d(TAG, "onLoadFinished: " + "valueThatJustPutInLoading = " + valueThatJustPutInLoading);
                    }
                }

                @Override
                public void onLoaderReset(Loader<List<ForecastWeatherData>> loader) {

                }
            };

    private static void bulkInsertForecastWeatherData(List<ForecastWeatherData> data){
        List<ContentValues> mForecastWeatherList = new ArrayList<>();
        for(int i = 0; i < data.size(); i++){
            ForecastWeatherData currentWeatherData = data.get(i);
            ContentValues values = bulkInsertHelper(currentWeatherData);
            mForecastWeatherList.add(values);
        }
        int rows = mContext.getContentResolver().bulkInsert(
                WeatherContract.ForecastWeatherEntry.CONTENT_URI,
                mForecastWeatherList.toArray(new ContentValues[data.size()])
        );
        Log.d(TAG, "bulkInsertForecastWeatherData: " + "rows inserted  = " + rows);
//        mLoaderManager.restartLoader(0, null, )
    }

    private static ContentValues bulkInsertHelper(ForecastWeatherData weatherData){
        ContentValues values = new ContentValues();
        values.put(WeatherContract.ForecastWeatherEntry.COLUMN_DATE_TIME, weatherData.date_time);
        values.put(WeatherContract.ForecastWeatherEntry.COLUMN_DATE, weatherData.date);
        values.put(WeatherContract.ForecastWeatherEntry.COLUMN_TIME, weatherData.time);
        values.put(WeatherContract.ForecastWeatherEntry.COLUMN_LAST_WEATHER_DATA_INSERTED_TIME, weatherData.lastUpdatedTime);
        values.put(WeatherContract.ForecastWeatherEntry.COLUMN_TEMP, weatherData.temp);
        values.put(WeatherContract.ForecastWeatherEntry.COLUMN_TEMP_FEELS_LIKE, weatherData.feelsLikeTemp);
        values.put(WeatherContract.ForecastWeatherEntry.COLUMN_MIN_TEMP, weatherData.minTemp);
        values.put(WeatherContract.ForecastWeatherEntry.COLUMN_MAX_TEMP, weatherData.maxTemp);
        values.put(WeatherContract.ForecastWeatherEntry.COLUMN_PRESSURE, weatherData.pressure);
        values.put(WeatherContract.ForecastWeatherEntry.COLUMN_PRESSURE_SEA_LEVEL, weatherData.sea_level_pressure);
        values.put(WeatherContract.ForecastWeatherEntry.COLUMN_GROUND_LEVEL, weatherData.ground_level_pressure);
        values.put(WeatherContract.ForecastWeatherEntry.COLUMN_HUMIDITY, weatherData.humidity);
        values.put(WeatherContract.ForecastWeatherEntry.COLUMN_WIND_SPEED, weatherData.windSpeed);
        values.put(WeatherContract.ForecastWeatherEntry.COLUMN_DEGREES, weatherData.windDirection);
        values.put(WeatherContract.ForecastWeatherEntry.COLUMN_CLOUDS, weatherData.clouds);
        values.put(WeatherContract.ForecastWeatherEntry.COLUMN_WEATHER_MAIN, weatherData.main);
        values.put(WeatherContract.ForecastWeatherEntry.COLUMN_WEATHER_DESCRIPTION, weatherData.description);
        values.put(WeatherContract.ForecastWeatherEntry.COLUMN_WEATHER_CONDITION_ICON, weatherData.icon);
        return values;
    }

}
