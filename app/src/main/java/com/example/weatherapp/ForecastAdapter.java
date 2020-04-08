package com.example.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.data.WeatherPreferences;
import com.example.weatherapp.utilities.WeatherOtherUtils;
import com.example.weatherapp.utilities.WeatherTimeUtils;

import java.text.DecimalFormat;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private final Context mContext;

    private final ForecastAdapterOnClickHandler mClickHandler;

    private Cursor mCursor;

    private static final int CURRENT_WEATHER_LAYOUT_ID = 0;

    private static final int FORECAST_WEATHER_LAYOUT_ID = 1;

    private String units;
    private String tempUnit;

    public interface ForecastAdapterOnClickHandler{
        void onClick(String date_time, int adapterPosition);
    }

    public ForecastAdapter(@NonNull Context context, ForecastAdapterOnClickHandler clickHandler){
        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int layoutID = 0;
        switch (viewType){
            case CURRENT_WEATHER_LAYOUT_ID : {
                layoutID = R.layout.list_weather_today;
                break;
            }
            case FORECAST_WEATHER_LAYOUT_ID : {
                layoutID = R.layout.forecast_list_item;
                break;
            }
        }

        View view = li.inflate(
                layoutID,
                parent,
                false
        );
        view.setFocusable(true);
        return new ForecastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        DecimalFormat df = new DecimalFormat("#.#");

        String time = mCursor.getString(MainActivity.INDEX_FORECAST_WEATHER_TIME);
        String date = mCursor.getString(MainActivity.INDEX_FORECAST_WEATHER_DATE);
        double current_temp = mCursor.getDouble(MainActivity.INDEX_FORECAST_WEATHER_TEMPERATURE);
        double min_temp = mCursor.getDouble(MainActivity.INDEX_FORECAST_WEATHER_MIN_TEMP);
        double max_temp = mCursor.getDouble(MainActivity.INDEX_FORECAST_WEATHER_MAX_TEMP);
        String icon = mCursor.getString(MainActivity.INDEX_FORECAST_WEATHER_CONDITION_ICON);
        String last_updated = mCursor.getString(MainActivity.INDEX_LAST_UPDATED);
        String main = mCursor.getString(MainActivity.INDEX_WEATHER_MAIN);

        units = WeatherPreferences.getUnits(mContext);
        if(units.equalsIgnoreCase("imperial")){
            tempUnit = "\u00b0"+"F";
        }
        else if(units.equalsIgnoreCase("standard")){
            tempUnit = "K";
        }
        else{
            tempUnit = "\u00b0"+"C";
        }
        if(position == 0){

            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(mContext);
            String city_name = preferences.getString("city_name", "");
            String visibility = "Visibility : " + preferences.getInt("visibility", 0)/1000.0 + "km";

            holder.tvDate.setText(WeatherTimeUtils.getDateToDisplayInCurrentWeatherData());
            holder.ivWeatherIcon.setImageResource(WeatherOtherUtils.weatherConditionIconAgainstGivenCode(icon));
            holder.tvMain.setText(main);
            holder.tvCurrentTemp.setText(df.format(current_temp)+tempUnit);
            holder.tvLastUpdated.setText(last_updated);
            holder.tvCityName.setText(city_name);
            if(preferences.getInt("visibility", 0) != -1) {
                holder.tvVisibility.setText(visibility);
                holder.tvVisibility.setVisibility(View.VISIBLE);
            }
            else{
                holder.tvVisibility.setVisibility(View.INVISIBLE);
            }
        }
        else {
            holder.tvTime.setText(time.substring(0, 5)); //substring call will remove last 2 zeros which represent seconds
            holder.tvDate.setText(date);
            holder.tvMain.setText(main);
            holder.ivWeatherIcon.setImageResource(WeatherOtherUtils.weatherConditionIconAgainstGivenCode(icon));
            holder.tvMinTemp.setText(df.format(min_temp)+tempUnit);
            holder.tvMaxTemp.setText(df.format(max_temp)+tempUnit);
        }
    }

    @Override
    public int getItemCount() {
        if(mCursor == null)
            return 0;
        return mCursor.getCount();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return CURRENT_WEATHER_LAYOUT_ID;
        }
        else{
            return FORECAST_WEATHER_LAYOUT_ID;
        }
    }

    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTime, tvDate, tvMain, tvMinTemp, tvMaxTemp,
                tvLastUpdated, tvCityName, tvCurrentTemp, tvVisibility;
        ImageView ivWeatherIcon;
        public ForecastAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvMain = itemView.findViewById(R.id.tvMain);
            tvMinTemp = itemView.findViewById(R.id.tvMinTemp);
            tvMaxTemp = itemView.findViewById(R.id.tvMaxTemp);
            ivWeatherIcon = itemView.findViewById(R.id.ivWeatherIcon);
            tvLastUpdated = itemView.findViewById(R.id.tvLastUpdated);
            tvCityName = itemView.findViewById(R.id.tvCityName);
            tvCurrentTemp = itemView.findViewById(R.id.tvCurrentTemp);
            tvVisibility = itemView.findViewById(R.id.tvVisibility);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            String currentWeather = tv_weather_data.getText().toString();
//            mClickHandler.onClick(currentWeather);
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            String date_time = mCursor.getString(MainActivity.INDEX_FORECAST_WEATHER_DATE_TIME);
            mClickHandler.onClick(date_time, adapterPosition);
        }
    }

    public void swapCursor(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
    }
}
