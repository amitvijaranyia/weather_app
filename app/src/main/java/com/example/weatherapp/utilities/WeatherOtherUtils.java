package com.example.weatherapp.utilities;

import android.content.Context;

import com.example.weatherapp.R;
import com.example.weatherapp.data.WeatherPreferences;

public class WeatherOtherUtils {

     static String formatHighLowTemp(Context context, String lowTemp, String highTemp){
        String units = WeatherPreferences.getUnits(context);
        if(units.equalsIgnoreCase("metric")){
            String formatted = "";
            formatted = highTemp + "°C / " + lowTemp + "°C";
            return formatted;
        }
        else if(units.equalsIgnoreCase("standard")){
            String formatted = "";
            formatted = highTemp + "K / " + lowTemp + "K";
            return formatted;
        }
        else if(units.equalsIgnoreCase("imperial")){
            String formatted = "";
            formatted = highTemp + "°F / " + lowTemp + "°F";
            return formatted;
        }
        String formatted = "";
        formatted = highTemp + "°C / " + lowTemp + "°C";
        return formatted;
    }

    public static int weatherConditionIconAgainstGivenCode(String iconCode){
        if(iconCode.equalsIgnoreCase("01d")){
            return R.drawable.clear_sky_01d;
        }
        else if(iconCode.equalsIgnoreCase("02d")){
            return R.drawable.few_clouds_02d;
        }
        else if(iconCode.equalsIgnoreCase("03d")){
            return R.drawable.scattered_cloud_03d;
        }
        else if(iconCode.equalsIgnoreCase("04d")){
            return R.drawable.broken_clouds_04d;
        }
        else if(iconCode.equalsIgnoreCase("09d")){
            return R.drawable.shower_rain_09d;
        }
        else if(iconCode.equalsIgnoreCase("10d")){
            return R.drawable.rain_10d;
        }
        else if(iconCode.equalsIgnoreCase("11d")){
            return R.drawable.thunderstorm_11d;
        }
        else if(iconCode.equalsIgnoreCase("13d")){
            return R.drawable.snow_13d;
        }
        else if(iconCode.equalsIgnoreCase("50d")){
            return R.drawable.mist_50d;
        }
        else if(iconCode.equalsIgnoreCase("01n")){
            return R.drawable.clear_sky_01n;
        }
        else if(iconCode.equalsIgnoreCase("02n")){
            return R.drawable.few_clouds_02n;
        }
        else if(iconCode.equalsIgnoreCase("03n")){
            return R.drawable.scattered_clouds_03n;
        }
        else if(iconCode.equalsIgnoreCase("04n")){
            return R.drawable.broken_clouds_04n;
        }
        else if(iconCode.equalsIgnoreCase("09n")){
            return R.drawable.shower_rain_09n;
        }
        else if(iconCode.equalsIgnoreCase("10n")){
            return R.drawable.rain_10n;
        }
        else if(iconCode.equalsIgnoreCase("11n")){
            return R.drawable.thunderstorm_11n;
        }
        else if(iconCode.equalsIgnoreCase("13n")){
            return R.drawable.snow_13n;
        }
        else if(iconCode.equalsIgnoreCase("50n")){
            return R.drawable.mist_50n;
        }
        else {
            return 0;
        }
    }

    /**
     N      348.75 - 11.25
     NNE    11.25 - 33.75
     NE     33.75 - 56.25
     ENE    56.25 - 78.75
     E      78.75 - 101.25
     ESE    101.25 - 123.75
     SE     123.75 - 146.25
     SSE    146.25 - 168.75
     S      168.75 - 191.25
     SSW    191.25 - 213.75
     SW     213.75 - 236.25
     WSW    236.25 - 258.75
     W      258.75 - 281.25
     WNW    281.25 - 303.75
     NW     303.75 - 326.25
     NNW    326.25 - 348.75
     */

    public static String getWindDirectionFromDegrees(double deg){
        if(deg >= 348.75) return "N";
        else if (deg >= 11.25 && deg <= 33.75) return "NNE";
        else if (deg >= 33.75 && deg <= 56.25) return "NE";
        else if (deg >=56.25 && deg <= 78.75) return "ENE";
        else if (deg >= 78.75 && deg <= 101.25) return "E";
        else if (deg >= 101.25 && deg <= 123.75) return "ESE";
        else if (deg >= 123.75 && deg <= 146.25) return "SE";
        else if (deg >= 146.25 && deg <= 168.75) return "SSE";
        else if (deg >= 168.75 && deg <= 191.25) return "S";
        else if (deg >= 191.25 && deg <= 213.75) return "SSW";
        else if (deg >= 213.75 && deg <= 236.25) return "SW";
        else if (deg >= 236.25 && deg <= 258.75) return "WSW";
        else if (deg >= 258.75 && deg <= 281.25) return "W";
        else if (deg >= 281.25 && deg <= 303.75) return "WNW";
        else if (deg >= 303.75 && deg <= 326.25) return "NW";
        else if (deg >= 326.25 && deg <= 348.75) return "NNW";
        return "Unknown";
    }
}
