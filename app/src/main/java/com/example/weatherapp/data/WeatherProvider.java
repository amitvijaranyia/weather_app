package com.example.weatherapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WeatherProvider extends ContentProvider {

    /** URI matcher code for the content URI for the forecast weather table */
    private static final int CODE_FORECAST_WEATHER = 100;

    /** URI matcher code for the content URI for a single forecast weather in the forecast weather table */
    private static final int CODE_FORECAST_WEATHER_WITH_DATE_TIME = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static WeatherDbHelper mDbHelper;

    // Static initializer. This is run the first time anything is called from this class.
    static {
        mUriMatcher.addURI(
                WeatherContract.CONTENT_AUTHORITY,
                WeatherContract.PATH_FORECAST_WEATHER,
                CODE_FORECAST_WEATHER
        );
        mUriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY,
                WeatherContract.PATH_FORECAST_WEATHER +"/*",
                CODE_FORECAST_WEATHER_WITH_DATE_TIME
        );
    }


    @Override
    public boolean onCreate(){
        mDbHelper = new WeatherDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        int match_code = mUriMatcher.match(uri);
        SQLiteDatabase mSqLiteOpenHelper = mDbHelper.getReadableDatabase();
        switch (match_code){
            case CODE_FORECAST_WEATHER : {

                cursor = mSqLiteOpenHelper.query(
                        WeatherContract.ForecastWeatherEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case CODE_FORECAST_WEATHER_WITH_DATE_TIME : {
                selection = WeatherContract.ForecastWeatherEntry.COLUMN_DATE_TIME + "=?";
                selectionArgs = new String[]{uri.getLastPathSegment()};

                cursor = mSqLiteOpenHelper.query(
                        WeatherContract.ForecastWeatherEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        int CODE_MATCH = mUriMatcher.match(uri);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        switch (CODE_MATCH){
            case CODE_FORECAST_WEATHER : {
                db.beginTransaction();
                int rowInserted = 0;
                try{
                    for(ContentValues value : values){
                        long _id = db.insert(
                                WeatherContract.ForecastWeatherEntry.TABLE_NAME,
                                null,
                                value
                        );
                        if(_id != -1){
                            rowInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }
                if(rowInserted > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowInserted;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        /**
         * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
         * deleted. However, if we do pass null and delete all of the rows in the table, we won't
         * know how many rows were deleted. According to the documentation for SQLiteDatabase,
         * passing "1" for the selection will delete all rows and return the number of rows
         * deleted, which is what the caller of this method expects.
         */
        if(selection == null){
            selection = "1";
        }
        int rowsDeleted = 0;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int CODE_MATCH = mUriMatcher.match(uri);
        switch (CODE_MATCH){
            case CODE_FORECAST_WEATHER : {
                rowsDeleted = db.delete(
                        WeatherContract.ForecastWeatherEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
            }
        }
        if(rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
