package com.metalcyborg.weather.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.metalcyborg.weather.R;
import com.metalcyborg.weather.util.WeatherUtils;
import com.metalcyborg.weather.data.Weather;
import com.metalcyborg.weather.data.WeatherDetails;

import java.util.ArrayList;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_3_HOURS_FORECAST = 0;
    private static final int TYPE_DAY_FORECAST = 1;
    private static final int TYPE_DETAILS = 2;
    private static final int TYPE_HEADER = 3;
    private List<Weather> m3HForecast = new ArrayList<>();
    private List<Weather> mDayForecast = new ArrayList<>();
    private WeatherDetails mCurrentWeatherDetails;
    private WeatherUtils.TemperatureUnits mTemperatureUnits = WeatherUtils.TemperatureUnits.CELSIUS;
    private WeatherUtils.PressureUnits mPressureUnits;
    private WeatherUtils.SpeedUnits mSpeedUnits;
    private WeatherUtils.TimeUnits mTimeUnits;

    public ForecastAdapter(WeatherUtils.TemperatureUnits temperatureUnits,
                           WeatherUtils.PressureUnits pressureUnits, WeatherUtils.SpeedUnits speedUnits,
                           WeatherUtils.TimeUnits timeUnits) {
        mTemperatureUnits = temperatureUnits;
        mPressureUnits = pressureUnits;
        mSpeedUnits = speedUnits;
        mTimeUnits = timeUnits;
    }

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
            case TYPE_DETAILS:
                view = inflater.inflate(R.layout.weather_details_item, parent, false);
                return new WeatherDetailsViewHolder(view);
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
            return mDayForecast.size() + 4;
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
            case TYPE_DETAILS:
                bindDetails((WeatherDetailsViewHolder) holder);
                break;
            case TYPE_HEADER:
                bindHeaderForecast((HeaderViewHolder) holder, position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 1) {
            return TYPE_DETAILS;
        } else if(position == 3){
            return TYPE_3_HOURS_FORECAST;
        } else if(position > 4){
            return TYPE_DAY_FORECAST;
        } else {
            return TYPE_HEADER;
        }
    }

    public void setDayForecast(List<Weather> dayForecast) {
        mDayForecast = dayForecast;
    }

    public void set3HForecast(List<Weather> forecast) {
        m3HForecast = forecast;
    }

    public void setWeatherDetails(WeatherDetails details) {
        mCurrentWeatherDetails = details;
    }

    private void bindDayForecast(DayForecastViewHolder holder, int position) {
        Weather weather = mDayForecast.get(position - 4);
        String date = WeatherUtils.convertLongToDateString(weather.getDateTime() * 1000);
        String day = WeatherUtils.convertLongToDayString(weather.getDateTime() * 1000);
        holder.mDateTextView.setText(date);
        holder.mDayTextView.setText(day);
        if (weather.getMain() != null) {
            String dayTemp = WeatherUtils.getTemperatureString(weather.getMain().getDayTemp(),
                    mTemperatureUnits);
            String nightTemp = WeatherUtils.getTemperatureString(weather.getMain().getNightTemp(),
                    mTemperatureUnits);
            holder.mDayTemperatureTextView.setText(dayTemp);
            holder.mNightTemperature.setText(nightTemp);
        }

        if(weather.getWeatherDescription() != null) {
            if(weather.getWeatherDescription().getIcon() != null) {
                int iconId = WeatherUtils.getIconId(weather.getWeatherDescription().getIcon());
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
            String time = WeatherUtils.convertLongToTimeString(m3HForecast.get(i).getDateTime() * 1000,
                    mTimeUnits);
            holder.mTimeArray[i].setText(time);
            String temp = WeatherUtils.getTemperatureString(m3HForecast.get(i).getMain().getDayTemp(),
                    mTemperatureUnits);
            holder.mTempArray[i].setText(temp);

            Weather.WeatherDescription description = m3HForecast.get(i).getWeatherDescription();
            if(description != null) {
                if(description.getIcon() != null) {
                    int iconId = WeatherUtils.getIconId(description.getIcon());
                    holder.mImageArray[i].setImageResource(iconId);
                }
            }
        }
    }

    private void bindDetails(WeatherDetailsViewHolder holder) {
        if(mCurrentWeatherDetails == null)
            return;

        String pressureString = WeatherUtils.getPressureString(mCurrentWeatherDetails.getPressure(),
                mPressureUnits);
        holder.mPressureTextView.setText(pressureString);

        holder.mHumidityTextView.setText(WeatherUtils.getHumidityString(
                mCurrentWeatherDetails.getHumidity()));

        String windSpeedStr = WeatherUtils.getSpeedString(mCurrentWeatherDetails.getWindSpeed(),
                mSpeedUnits);
        holder.mWindSpeedTextView.setText(windSpeedStr);

        String sunriseTimeStr = WeatherUtils.convertLongToTimeString(mCurrentWeatherDetails.getSunrise() * 1000,
                mTimeUnits);
        holder.mSunriseTextView.setText(sunriseTimeStr);

        String sunsetTimeStr = WeatherUtils.convertLongToTimeString(mCurrentWeatherDetails.getSunset() * 1000,
                mTimeUnits);
        holder.mSunsetTextView.setText(sunsetTimeStr);

        String dayLightStr = WeatherUtils.convertLongToDurationString(
                (mCurrentWeatherDetails.getSunset() - mCurrentWeatherDetails.getSunrise()) * 1000
        );
        holder.mDayLightTime.setText(dayLightStr);

        WeatherUtils.Wind wind = WeatherUtils.getWindDirectionByAngle(mCurrentWeatherDetails.getWindDeg());
        holder.mWindDegImageView.setImageResource(getWindDegIcon(wind));
    }

    private void bindHeaderForecast(HeaderViewHolder holder, int position) {
        if(position == 0) {
            holder.mHeaderTextView.setText(R.string.details_header);
        } else if(position == 2) {
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

    private int getWindDegIcon(WeatherUtils.Wind wind) {
        switch (wind) {

            case N:
                return R.drawable.ic_arrow_up_grey600_24dp;
            case S:
                return R.drawable.ic_arrow_down_grey600_24dp;
            case W:
                return R.drawable.ic_arrow_left_grey600_24dp;
            case E:
                return R.drawable.ic_arrow_right_grey600_24dp;
            case NW:
                return R.drawable.ic_arrow_top_left_grey600_24dp;
            case NE:
                return R.drawable.ic_arrow_top_right_grey600_24dp;
            case SW:
                return R.drawable.ic_arrow_bottom_left_grey600_24dp;
            case SE:
                return R.drawable.ic_arrow_bottom_right_grey600_24dp;
            default:
                return R.drawable.ic_arrow_up_grey600_24dp;
        }
    }

    public class DayForecastViewHolder extends RecyclerView.ViewHolder {

        private TextView mDateTextView;
        private TextView mDayTextView;
        private ImageView mIconImageView;
        private TextView mDayTemperatureTextView;
        private TextView mNightTemperature;

        public DayForecastViewHolder(View itemView) {
            super(itemView);

            mDateTextView = (TextView) itemView.findViewById(R.id.date);
            mDayTextView = (TextView) itemView.findViewById(R.id.day);
            mIconImageView = (ImageView) itemView.findViewById(R.id.icon);
            mDayTemperatureTextView = (TextView) itemView.findViewById(R.id.dayTemperature);
            mNightTemperature = (TextView) itemView.findViewById(R.id.nightTemperature);
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

    private class WeatherDetailsViewHolder extends RecyclerView.ViewHolder {

        private TextView mPressureTextView;
        private TextView mHumidityTextView;
        private TextView mWindSpeedTextView;
        private TextView mSunriseTextView;
        private TextView mSunsetTextView;
        private TextView mDayLightTime;
        private ImageView mWindDegImageView;

        public WeatherDetailsViewHolder(View itemView) {
            super(itemView);
            mPressureTextView = (TextView) itemView.findViewById(R.id.pressure);
            mHumidityTextView = (TextView) itemView.findViewById(R.id.humidity);
            mWindSpeedTextView = (TextView) itemView.findViewById(R.id.windSpeed);
            mSunriseTextView = (TextView) itemView.findViewById(R.id.sunrise);
            mSunsetTextView = (TextView) itemView.findViewById(R.id.sunset);
            mDayLightTime = (TextView) itemView.findViewById(R.id.dayLightTime);
            mWindDegImageView = (ImageView) itemView.findViewById(R.id.windDeg);
        }
    }
}
