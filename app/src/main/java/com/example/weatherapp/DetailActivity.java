package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherapp.data.WeatherContract;
import com.example.weatherapp.data.WeatherPreferences;
import com.example.weatherapp.utilities.WeatherOtherUtils;
import com.example.weatherapp.utilities.WeatherTimeUtils;

import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "tag";

    String weatherSummary;

    TextView tvDate, tvTime, tvDescription, tvHighTemp, tvLowTemp, tvFeelsLikeTemp, tvWindspeed,
            tvWindDirection, tvClouds, tvHumidity, tvPressure, tvPressureAtSeaLevel, tvPressureAtGroundLevel;

    ImageView ivWeatherIcon;

    ForecastWeatherData weatherData;

    String units;

    private final String[] DETAIL_ACTIVITY_WEATHER_DATA_SELECTION = new String[]{
            WeatherContract.ForecastWeatherEntry.COLUMN_DATE,
            WeatherContract.ForecastWeatherEntry.COLUMN_TIME,
            WeatherContract.ForecastWeatherEntry.COLUMN_LAST_WEATHER_DATA_INSERTED_TIME,
            WeatherContract.ForecastWeatherEntry.COLUMN_TEMP,
            WeatherContract.ForecastWeatherEntry.COLUMN_TEMP_FEELS_LIKE,
            WeatherContract.ForecastWeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.ForecastWeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.ForecastWeatherEntry.COLUMN_PRESSURE,
            WeatherContract.ForecastWeatherEntry.COLUMN_PRESSURE_SEA_LEVEL,
            WeatherContract.ForecastWeatherEntry.COLUMN_GROUND_LEVEL,
            WeatherContract.ForecastWeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.ForecastWeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.ForecastWeatherEntry.COLUMN_DEGREES,
            WeatherContract.ForecastWeatherEntry.COLUMN_CLOUDS,
            WeatherContract.ForecastWeatherEntry.COLUMN_WEATHER_MAIN,
            WeatherContract.ForecastWeatherEntry.COLUMN_WEATHER_DESCRIPTION,
            WeatherContract.ForecastWeatherEntry.COLUMN_WEATHER_CONDITION_ICON,
    };

    public static final int INDEX_COLUMN_DATE = 0;
    public static final int INDEX_COLUMN_TIME = 1;
    public static final int INDEX_COLUMN_LAST_WEATHER_DATA_INSERTED_TIME = 2;
    public static final int INDEX_COLUMN_TEMP = 3;
    public static final int INDEX_COLUMN_TEMP_FEELS_LIKE = 4;
    public static final int INDEX_COLUMN_MIN_TEMP = 5;
    public static final int INDEX_COLUMN_MAX_TEMP = 6;
    public static final int INDEX_COLUMN_PRESSURE = 7;
    public static final int INDEX_COLUMN_PRESSURE_SEA_LEVEL = 8;
    public static final int INDEX_COLUMN_GROUND_LEVEL = 9;
    public static final int INDEX_COLUMN_HUMIDITY = 10;
    public static final int INDEX_COLUMN_WIND_SPEED = 11;
    public static final int INDEX_COLUMN_DEGREES = 12;
    public static final int INDEX_COLUMN_CLOUDS = 13;
    public static final int INDEX_COLUMN_WEATHER_MAIN = 14;
    public static final int INDEX_COLUMN_WEATHER_DESCRIPTION = 15;
    public static final int INDEX_COLUMN_WEATHER_CONDITION_ICON = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvDescription = findViewById(R.id.tvDescription);
        tvHighTemp = findViewById(R.id.tvMaxTemperature);
        tvLowTemp = findViewById(R.id.tvMinTemperature);
        tvFeelsLikeTemp = findViewById(R.id.tvFeelsLikeTempValue);
        tvWindspeed = findViewById(R.id.tvWindSpeedValue);
        tvWindDirection = findViewById(R.id.tvWindDirectionValue);
        tvClouds = findViewById(R.id.tvCloudsValue);
        tvHumidity = findViewById(R.id.tvHumidityValue);
        tvPressure = findViewById(R.id.tvPressureValue);
        tvPressureAtSeaLevel = findViewById(R.id.tvPressureAtSeaLevelValue);
        tvPressureAtGroundLevel = findViewById(R.id.tvPressureAtGroundLevelValue);

        ivWeatherIcon = findViewById(R.id.ivWeatherIcon);

        units = WeatherPreferences.getUnits(this);

        Intent intentThatStartedThisActivity = getIntent();

        if(intentThatStartedThisActivity != null && intentThatStartedThisActivity.getData() != null){
            Uri specificDateTimeWeatherDataUri = intentThatStartedThisActivity.getData();
            Cursor cursor = getContentResolver().query(
                    specificDateTimeWeatherDataUri,
                    DETAIL_ACTIVITY_WEATHER_DATA_SELECTION,
                    null,
                    null,
                    null
            );
            if(cursor != null)
                cursor.moveToFirst();
            weatherSummary = weatherSummary(cursor);
        }

        bindWeatherDetails();
    }

    public String weatherSummary(Cursor cursor){
        if(cursor.getCount() != 0) {
            String date = cursor.getString(INDEX_COLUMN_DATE);
            String time = cursor.getString(INDEX_COLUMN_TIME);
            String last_updated = cursor.getString(INDEX_COLUMN_LAST_WEATHER_DATA_INSERTED_TIME);
            double temp = cursor.getDouble(INDEX_COLUMN_TEMP);
            double feels_like_temp = cursor.getDouble(INDEX_COLUMN_TEMP_FEELS_LIKE);
            double min_temp = cursor.getDouble(INDEX_COLUMN_MIN_TEMP);
            double max_temp = cursor.getDouble(INDEX_COLUMN_MAX_TEMP);
            double pressure = cursor.getDouble(INDEX_COLUMN_PRESSURE);
            double pressure_sea_level = cursor.getDouble(INDEX_COLUMN_PRESSURE_SEA_LEVEL);
            double pressure_ground_level = cursor.getDouble(INDEX_COLUMN_GROUND_LEVEL);
            double humidity = cursor.getDouble(INDEX_COLUMN_HUMIDITY);
            double wind_speed = cursor.getDouble(INDEX_COLUMN_WIND_SPEED);
            double wind_direction = cursor.getDouble(INDEX_COLUMN_DEGREES);
            double clouds = cursor.getDouble(INDEX_COLUMN_CLOUDS);
            String main = cursor.getString(INDEX_COLUMN_WEATHER_MAIN);
            String description = cursor.getString(INDEX_COLUMN_WEATHER_DESCRIPTION);
            String icon = cursor.getString(INDEX_COLUMN_WEATHER_CONDITION_ICON);

            weatherData = new ForecastWeatherData(
                    null,
                    date,
                    time,
                    last_updated,
                    main,
                    description,
                    icon,
                    temp,
                    feels_like_temp,
                    min_temp,
                    max_temp,
                    pressure,
                    pressure_sea_level,
                    pressure_ground_level,
                    humidity,
                    wind_speed,
                    wind_direction,
                    clouds
            );

            String weatherSummary = date + "\n" +
                    time + "\n" +
                    last_updated + "\n" +
                    temp + "\n" +
                    feels_like_temp + "\n" +
                    min_temp + "\n" +
                    max_temp + "\n" +
                    pressure + "\n" +
                    pressure_sea_level + "\n" +
                    pressure_ground_level + "\n" +
                    humidity + "\n" +
                    wind_speed + "\n" +
                    wind_direction + "\n" +
                    clouds + "\n" +
                    main + "\n" +
                    description + "\n" +
                    icon + "\n";
            return weatherSummary;

        }
        return null;
    }

    public void bindWeatherDetails(){
        String windUnit = units.equalsIgnoreCase("imperial") ? "mph" : "m/s";
        String tempUnit = "";
        if(units.equalsIgnoreCase("imperial")){
            tempUnit = "\u00b0"+"F";
        }
        else if(units.equalsIgnoreCase("standard")){
            tempUnit = "K";
        }
        else{
            tempUnit = "\u00b0"+"C";
        }
        DecimalFormat df = new DecimalFormat("#.#");

        if(weatherData.date == null){
            tvDate.setText(WeatherTimeUtils.getDateToDisplayInCurrentWeatherData());
        }else{
            tvDate.setText(weatherData.date);
        }

        if(weatherData.time == null){
            tvTime.setText(WeatherTimeUtils.getTimeToDisplayInCurrentWeatherData());
        }else {
            tvTime.setText(weatherData.time);
        }

        tvDescription.setText(weatherData.description);
        tvHighTemp.setText(df.format(weatherData.maxTemp)+tempUnit);
        tvLowTemp.setText(df.format(weatherData.minTemp)+tempUnit);
        tvFeelsLikeTemp.setText(df.format(weatherData.feelsLikeTemp)+tempUnit);
        tvPressure.setText(df.format(weatherData.pressure)+"hPa");
        tvPressureAtSeaLevel.setText(df.format(weatherData.sea_level_pressure)+"hPa");
        tvPressureAtGroundLevel.setText(df.format(weatherData.ground_level_pressure)+"hPa");
        tvHumidity.setText(df.format(weatherData.humidity)+"%");
        tvWindspeed.setText(df.format(weatherData.windSpeed)+windUnit);
        tvWindDirection.setText(WeatherOtherUtils.getWindDirectionFromDegrees(weatherData.windDirection));
        tvClouds.setText(df.format(weatherData.clouds)+"%");

        ivWeatherIcon.setImageResource(WeatherOtherUtils.weatherConditionIconAgainstGivenCode(weatherData.icon));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.shareDetails){
//            shareDetailWeatherData();
            shareDetails();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void shareDetailWeatherData(){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, weatherSummary);
        startActivity(Intent.createChooser(sharingIntent, "Weather Details"));
    }

    public void shareDetails(){
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(weatherSummary)
                .getIntent();
        startActivity(Intent.createChooser(shareIntent, "Weather Details"));
    }
}
