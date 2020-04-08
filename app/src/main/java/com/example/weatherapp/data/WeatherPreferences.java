package com.example.weatherapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.preference.PreferenceManager;

public class WeatherPreferences {

    private static final String TAG = "tag";

    public static final String PREF_CITY_NAME = "city_name";

    public static final String PREF_COORD_LAT = "coord_lat";

    public static final String  PREF_COORD_LONG = "coord_long";

    private static final String DEFAULT_WEATHER_LOCATION_ZIP_CODE = "110086,in";

    private static final double[] DEFAULT_WEATHER_COORDINATES = {28.7041, 77.1025};

    /**
     * Helper method to handle setting location details in Preferences (City Name, Latitude,
     * Longitude)
     */
    static public void setLocationDetails(Context c, String cityName, double lat, double lon) {
        /** This will be implemented in a future lesson **/
    }

    /**
     * Helper method to handle setting a new location in preferences.  When this happens
     * the database may need to be cleared.
     */
    static public void setLocation(Context c, String locationSetting, double lat, double lon) {
        /** This will be implemented in a future lesson **/
    }

    /**
     * Resets the stored location coordinates.
     */
    static public void resetLocationCoordinates(Context c) {
        /** This will be implemented in a future lesson **/
    }

    /**
     * Returns the location currently set in Preferences.
     */
    public static String getPreferredWeatherLocation(Context context) {
        /** This will be implemented in a future lesson **/
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String location_zip = preferences.getString("location_zip", "");
        String pref_country_name = preferences.getString("pref_country_name", "");

        String preferredWeatherLocation = location_zip + "," + pref_country_name;
        if(preferredWeatherLocation.length() != 1){
            return preferredWeatherLocation;
        }
        else {
            return getDefaultWeatherLocation();
        }
    }

    /**
     * Returns units in which weather data will be shown
     * @param context
     * @return 1.metric(celsius) 2.imperial(fahrenheit) 3.default(kelvin)
     */
    public static String  getUnits(Context context) {
        /** This will be implemented in a future lesson **/
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String units = preferences.getString("units_preference", "");
            return units;
    }

    /**
     * Returns the location coordinates associated with the location.  Note that these coordinates
     * may not be set, which results in (0,0) being returned. (conveniently, 0,0 is in the middle
     * of the ocean off the west coast of Africa)
     *
     * @param context Used to get the SharedPreferences
     * @return An array containing the two coordinate values.
     */
    public static double[] getLocationCoordinates(Context context) {
        return getDefaultWeatherCoordinates();
    }

    /**
     * Returns true if the latitude and longitude values are available. The latitude and
     * longitude will not be available until the lesson where the PlacePicker API is taught.
     *
     * @param context used to get the SharedPreferences
     * @return true if lat/long are set
     */
    public static boolean isLocationLatLonAvailable(Context context) {
        /** This will be implemented in a future lesson **/
        return false;
    }

    public static String getPreferredLanguage(Context context){
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String preferredLanguage = preferences.getString("pref_weather_data_language", "");
        return preferredLanguage;
    }

    private static String getDefaultWeatherLocation() {
        /** This will be implemented in a future lesson **/
        return DEFAULT_WEATHER_LOCATION_ZIP_CODE;
    }

    public static double[] getDefaultWeatherCoordinates() {
        /** This will be implemented in a future lesson **/
        return DEFAULT_WEATHER_COORDINATES;
    }

}
