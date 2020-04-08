package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.data.WeatherContract;
import com.example.weatherapp.data.WeatherPreferences;
import com.example.weatherapp.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements ForecastAdapter.ForecastAdapterOnClickHandler,
        LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {
    /**
     * The columns of data that we are interested in displaying within our MainActivity's list of
     * weather data.
     */
    public static final String[] MAIN_FORECAST_PROJECTION = {
            WeatherContract.ForecastWeatherEntry.COLUMN_DATE,
            WeatherContract.ForecastWeatherEntry.COLUMN_TIME,
            WeatherContract.ForecastWeatherEntry.COLUMN_TEMP,
            WeatherContract.ForecastWeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.ForecastWeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.ForecastWeatherEntry.COLUMN_WEATHER_CONDITION_ICON,
            WeatherContract.ForecastWeatherEntry.COLUMN_DATE_TIME,
            WeatherContract.ForecastWeatherEntry.COLUMN_LAST_WEATHER_DATA_INSERTED_TIME,
            WeatherContract.ForecastWeatherEntry.COLUMN_WEATHER_MAIN
    };

    /**
     * We store the indices of the values in the array of Strings above to more quickly be able to
     * access the data from our query. If the order of the Strings above changes, these indices
     * must be adjusted to match the order of the Strings.
     */
    public static final int INDEX_FORECAST_WEATHER_DATE = 0;
    public static final int INDEX_FORECAST_WEATHER_TIME = 1;
    public static final int INDEX_FORECAST_WEATHER_TEMPERATURE = 2;
    public static final int INDEX_FORECAST_WEATHER_MIN_TEMP = 3;
    public static final int INDEX_FORECAST_WEATHER_MAX_TEMP = 4;
    public static final int INDEX_FORECAST_WEATHER_CONDITION_ICON = 5;
    public static final int INDEX_FORECAST_WEATHER_DATE_TIME = 6;
    public static final int INDEX_LAST_UPDATED = 7;
    public static final int INDEX_WEATHER_MAIN = 8;

    private static final String TAG = "tag";

    RecyclerView rvForecastWeatherData;
    ProgressBar pbLoadingIndicator;
    TextView tvErrorMessage;

    SwipeRefreshLayout mSwipeRefreshLayout;

    ForecastAdapter mWeatherAdapter;

    private int mPosition = RecyclerView.NO_POSITION;

    private int requestCode = 1;

    private URL urlBeforeSettingsActivityStarted;

    int LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0f);

        rvForecastWeatherData = findViewById(R.id.rvForecastWeatherData);
        pbLoadingIndicator = findViewById(R.id.pbLoadingIndication);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        );

        mWeatherAdapter = new ForecastAdapter(this, this);
        rvForecastWeatherData.setHasFixedSize(true);
        rvForecastWeatherData.setLayoutManager(layoutManager);
        rvForecastWeatherData.setAdapter(mWeatherAdapter);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefresh);

        PreferenceManager.getDefaultSharedPreferences(this)
                .edit().putString("loading", "none").commit();

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);


        showLoading();
        getLoaderManager().initLoader(LOADER_ID, null, this);

        //this will insert forecast weather data into out database
        InsertDataIntoDatabase.insertIntoDatabase(this, getLoaderManager());

        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        setSwipeRefreshLayout(true);
//                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id == LOADER_ID){
            Uri forecastQueryUri = WeatherContract.ForecastWeatherEntry.CONTENT_URI;
            CursorLoader forecastWeatherLoader = new CursorLoader(
                    this, forecastQueryUri,
                    MAIN_FORECAST_PROJECTION,
                    null,
                    null,
                    null
            );
            return forecastWeatherLoader;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mWeatherAdapter.swapCursor(data);
        if(mPosition == RecyclerView.NO_POSITION)
            mPosition = 0;
        rvForecastWeatherData.smoothScrollToPosition(mPosition);
        if(data.getCount() != 0){
            showForecastWeatherData();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mWeatherAdapter.swapCursor(null);
    }

    public void invalidateData(){
        pbLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_refresh){
            setSwipeRefreshLayout(true);
            return true;
        }
        else if(item.getItemId() == R.id.menu_map_location){
            openLocationInMap();
            return true;
        }
        else if(item.getItemId() == R.id.menu_settings_activity){
            String zip_code = WeatherPreferences
                    .getPreferredWeatherLocation(MainActivity.this);
            urlBeforeSettingsActivityStarted = NetworkUtils.buildForecastUrlWithZIP(MainActivity.this, zip_code);
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivityForResult(i, requestCode);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            String zip_code = WeatherPreferences
                    .getPreferredWeatherLocation(MainActivity.this);
            URL urlAfterSettingsActivityFinished = NetworkUtils.buildForecastUrlWithZIP(MainActivity.this, zip_code);
            if(!urlBeforeSettingsActivityStarted.equals(urlAfterSettingsActivityFinished)) {
                invalidateData();
                InsertDataIntoDatabase.restartLoader(MainActivity.this, getLoaderManager());
            }
        }
    }


    public void openLocationInMap(){
        String address = WeatherPreferences.getPreferredWeatherLocation(MainActivity.this);
        String map = "http://maps.google.co.in/maps?q=" + address;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        startActivity(intent);
    }


    public void showForecastWeatherData(){
        tvErrorMessage.setVisibility(View.INVISIBLE);
        rvForecastWeatherData.setVisibility(View.VISIBLE);
        pbLoadingIndicator.setVisibility(View.INVISIBLE);
    }


    public void showErrorMessage(){
        tvErrorMessage.setVisibility(View.VISIBLE);
        rvForecastWeatherData.setVisibility(View.INVISIBLE);
        pbLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    private void showLoading() {
        /* Then, hide the weather data */
        rvForecastWeatherData.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        pbLoadingIndicator.setVisibility(View.VISIBLE);
        tvErrorMessage.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(String date_time, int adapterPosition) {
        Uri specificDateTimeWeatherDataUri = null;
        if(adapterPosition != 0) {
            specificDateTimeWeatherDataUri = WeatherContract
                    .ForecastWeatherEntry
                    .CONTENT_URI.buildUpon()
                    .appendPath(date_time)
                    .build();
        }
        else{
            specificDateTimeWeatherDataUri = WeatherContract
                    .ForecastWeatherEntry
                    .CONTENT_URI.buildUpon()
                    .appendPath("Amit")
                    .build();
        }
        Intent i = new Intent(MainActivity.this, DetailActivity.class);
        i.setData(specificDateTimeWeatherDataUri);
        startActivity(i);
//        Log.d("posn", "onClick: " + adapterPosition);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String isFailed = sharedPreferences.getString("loading", "");
        if(isFailed.equalsIgnoreCase("failed")){
            showForecastWeatherData();
            Toast.makeText(this, "Failed to refresh weather data", Toast.LENGTH_LONG).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }
        else if(isFailed.equalsIgnoreCase("success")){
            mSwipeRefreshLayout.setRefreshing(false);
        }
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("loading", "none").apply();
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    public void setSwipeRefreshLayout(boolean showProgressBar){
        invalidateData();
        InsertDataIntoDatabase.restartLoader(MainActivity.this, getLoaderManager());
    }

}
