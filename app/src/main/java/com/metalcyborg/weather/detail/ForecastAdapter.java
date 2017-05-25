package com.metalcyborg.weather.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.metalcyborg.weather.R;
import com.metalcyborg.weather.Utils;
import com.metalcyborg.weather.data.Weather;

import java.util.ArrayList;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_3_HOURS_FORECAST = 0;
    private static final int TYPE_DAY_FORECAST = 1;
    private static final int TYPE_HEADER = 2;
    private List<Weather> m3HForecast = new ArrayList<>();
    private List<Weather> mDayForecast = new ArrayList<>();
    private Utils.TemperatureUnits mTemperatureUnits = Utils.TemperatureUnits.CELSIUS;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
            case TYPE_3_HOURS_FORECAST:
                view = inflater.inflate(R.layout.three_hours_forecast_items, parent, false);
                return new ThreeHoursForecastViewHolder(view);
            case TYPE_DAY_FORECAST:
                view = inflater.inflate(R.layout.day_forecast_item, parent, false);
                return new DayForecastViewHolder(view);
            case TYPE_HEADER:
                view = inflater.inflate(R.layout.forecast_header, parent, false);
                return new HeaderViewHolder(view);
        }

        return null;
    }

    @Override
    public int getItemCount() {
        if(mDayForecast.size() == 0) {
            return 0;
        } else {
            return mDayForecast.size() + 2;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_3_HOURS_FORECAST:
                bind3HoursForecast((ThreeHoursForecastViewHolder) holder);
                break;
            case TYPE_DAY_FORECAST:
                bindDayForecast((DayForecastViewHolder) holder, position);
                break;
            case TYPE_HEADER:
                bindHeaderForecast((HeaderViewHolder) holder, position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 1) {
            return TYPE_3_HOURS_FORECAST;
        } else if(position > 2){
            return TYPE_DAY_FORECAST;
        } else {
            return TYPE_HEADER;
        }
    }

    public void setDayForecast(List<Weather> dayForecast, Utils.TemperatureUnits tempUnits) {
        mTemperatureUnits = tempUnits;
        mDayForecast = dayForecast;
    }

    public void set3HForecast(List<Weather> forecast, Utils.TemperatureUnits tempUnits) {
        mTemperatureUnits = tempUnits;
        m3HForecast = forecast;
    }

    private void bindDayForecast(DayForecastViewHolder holder, int position) {
        Weather weather = mDayForecast.get(position - 2);
        String date = Utils.convertLongToDateString(weather.getDateTime() * 1000);
        String day = Utils.convertLongToDayString(weather.getDateTime() * 1000);
        holder.mDateTextView.setText(date);
        holder.mDayTextView.setText(day);
        if (weather.getMain() != null) {
            String dayTemp = Utils.getTemperatureString(weather.getMain().getDayTemp(),
                    mTemperatureUnits);
            holder.mDayTemperatureTextView.setText(dayTemp);
        }

        if(weather.getWeatherDescription() != null) {
            if(weather.getWeatherDescription().getIcon() != null) {
                int iconId = Utils.getIconId(weather.getWeatherDescription().getIcon());
                if(iconId != -1) {
                    holder.mIconImageView.setImageResource(iconId);
                }
            }
        }
    }

    private void bind3HoursForecast(ThreeHoursForecastViewHolder holder) {
        if(m3HForecast == null || m3HForecast.size() < ThreeHoursForecastViewHolder.FORECAST_COUNT)
            return;

        for(int i = 0; i < ThreeHoursForecastViewHolder.FORECAST_COUNT; ++i) {
            String time = Utils.convertLongToTimeString(m3HForecast.get(i).getDateTime() * 1000);
            holder.mTimeArray[i].setText(time);
            String temp = Utils.getTemperatureString(m3HForecast.get(i).getMain().getDayTemp(),
                    mTemperatureUnits);
            holder.mTempArray[i].setText(temp);

            Weather.WeatherDescription description = m3HForecast.get(i).getWeatherDescription();
            if(description != null) {
                if(description.getIcon() != null) {
                    int iconId = Utils.getIconId(description.getIcon());
                    holder.mImageArray[i].setImageResource(iconId);
                }
            }
        }
    }

    private void bindHeaderForecast(HeaderViewHolder holder, int position) {
        if(position == 0) {
            holder.mHeaderTextView.setText(R.string.header_3h_forecast);
        } else {
            holder.mHeaderTextView.setText(R.string.header_daily_forecast);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView mHeaderTextView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mHeaderTextView = (TextView) itemView.findViewById(R.id.header);
        }
    }

    public class DayForecastViewHolder extends RecyclerView.ViewHolder {

        private TextView mDateTextView;
        private TextView mDayTextView;
        private ImageView mIconImageView;
        private TextView mDayTemperatureTextView;

        public DayForecastViewHolder(View itemView) {
            super(itemView);

            mDateTextView = (TextView) itemView.findViewById(R.id.date);
            mDayTextView = (TextView) itemView.findViewById(R.id.day);
            mIconImageView = (ImageView) itemView.findViewById(R.id.icon);
            mDayTemperatureTextView = (TextView) itemView.findViewById(R.id.dayTemperature);
        }
    }

    public class ThreeHoursForecastViewHolder extends RecyclerView.ViewHolder {

        public static final int FORECAST_COUNT = 6;
        private TextView[] mTimeArray = new TextView[FORECAST_COUNT];
        private ImageView[] mImageArray = new ImageView[FORECAST_COUNT];
        private TextView[] mTempArray = new TextView[FORECAST_COUNT];

        public ThreeHoursForecastViewHolder(View itemView) {
            super(itemView);

            mTimeArray[0] = (TextView) itemView.findViewById(R.id.time1);
            mTimeArray[1] = (TextView) itemView.findViewById(R.id.time2);
            mTimeArray[2] = (TextView) itemView.findViewById(R.id.time3);
            mTimeArray[3] = (TextView) itemView.findViewById(R.id.time4);
            mTimeArray[4] = (TextView) itemView.findViewById(R.id.time5);
            mTimeArray[5] = (TextView) itemView.findViewById(R.id.time6);

            mImageArray[0] = (ImageView) itemView.findViewById(R.id.image1);
            mImageArray[1] = (ImageView) itemView.findViewById(R.id.image2);
            mImageArray[2] = (ImageView) itemView.findViewById(R.id.image3);
            mImageArray[3] = (ImageView) itemView.findViewById(R.id.image4);
            mImageArray[4] = (ImageView) itemView.findViewById(R.id.image5);
            mImageArray[5] = (ImageView) itemView.findViewById(R.id.image6);

            mTempArray[0] = (TextView) itemView.findViewById(R.id.temp1);
            mTempArray[1] = (TextView) itemView.findViewById(R.id.temp2);
            mTempArray[2] = (TextView) itemView.findViewById(R.id.temp3);
            mTempArray[3] = (TextView) itemView.findViewById(R.id.temp4);
            mTempArray[4] = (TextView) itemView.findViewById(R.id.temp5);
            mTempArray[5] = (TextView) itemView.findViewById(R.id.temp6);
        }
    }
}
