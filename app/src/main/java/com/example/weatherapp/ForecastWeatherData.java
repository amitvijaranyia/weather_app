package com.example.weatherapp;

public class ForecastWeatherData {

    String date_time, date, time, lastUpdatedTime, main, description, icon;

    double temp, feelsLikeTemp, minTemp, maxTemp, pressure,
            sea_level_pressure, ground_level_pressure, humidity, windSpeed, windDirection, clouds;

    public ForecastWeatherData(String date_time, String date, String time,
                               String lastUpdatedTime, String main, String description,
                               String icon, double temp, double feelsLikeTemp, double minTemp,
                               double maxTemp, double pressure, double sea_level_pressure,
                               double ground_level_pressure, double humidity, double windSpeed,
                               double windDirection, double clouds) {
        this.date_time = date_time;
        this.date = date;
        this.time = time;
        this.lastUpdatedTime = lastUpdatedTime;
        this.main = main;
        this.description = description;
        this.icon = icon;
        this.temp = temp;
        this.feelsLikeTemp = feelsLikeTemp;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.pressure = pressure;
        this.sea_level_pressure = sea_level_pressure;
        this.ground_level_pressure = ground_level_pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.clouds = clouds;
    }
}
