package com.example.weatherapp.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherTimeUtils {

    private static final String TAG = "tag";

    public static String formatDateAndTime(String dt){
        String formatted = "";
        if(getTodOrTom(dt) != null){
            formatted = getTodOrTom(dt) + " ";
        }

        String todayDate = null;
        try {
            todayDate = getDate(dt);
//            Log.d(TAG, "formatDateAndTime: " + todayDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        Date dt1= null;
        try {
            dt1 = format1.parse(todayDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat format2 = new SimpleDateFormat("dd-MMM");
        String finalDate = format2.format(dt1);

        formatted += finalDate;

        return formatted;
    }

    public static String getTodOrTom(String dt) {
        String owmDate = null;
        try {
            owmDate = getDate(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date date = new Date();
        DateFormat formattedDate = new SimpleDateFormat("dd-MMMM-yyyy");
        String todayDate = formattedDate.format(date);

        if(owmDate.substring(0,2).equalsIgnoreCase(todayDate.substring(0, 2))){
            return "Today";
        }
        if(Integer.valueOf(owmDate.substring(0, 2)) == Integer.valueOf(todayDate.substring(0, 2)) + 1){
            return "Tomorrow";
        }
        return null;
    }

    public static String getDate(String dt) throws ParseException {
        String date = "";
        for(int i = 0; i < dt.length(); i++){
            if(dt.charAt(i) == ' '){
                break;
            }
            date = date + dt.charAt(i);
        }
        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
        Date dt1 = format1.parse(date);
        DateFormat format2=new SimpleDateFormat("dd-MM-yyyy");
        String finalDate=format2.format(dt1);
        return finalDate;
    }

    public static String getTime(String dt){
        String time = "";
        for(int i = dt.length()-1; i >= 0; i--){
            if(dt.charAt(i) == ' '){
                break;
            }
            time = dt.charAt(i) + time;
        }
        return time;
    }

    public static String getCurrentTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MMM");
        Date date = new Date();
        String currentTime = formatter.format(date);
        return currentTime;
    }

    public static String getDateToDisplayInCurrentWeatherData(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM");
        String str = formatter.format(date);

        return "Today, " + str;
    }

    public static String getTimeToDisplayInCurrentWeatherData(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String str = formatter.format(date);
        return str;
    }

}
