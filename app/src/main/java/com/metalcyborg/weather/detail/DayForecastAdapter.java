package com.metalcyborg.weather.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.metalcyborg.weather.R;
import com.metalcyborg.weather.Utils;
import com.metalcyborg.weather.data.Weather;

import java.util.ArrayList;
import java.util.List;

public class DayForecastAdapter extends RecyclerView.Adapter<DayForecastAdapter.DayForecastViewHolder> {

    private List<Weather> mItems = new ArrayList<>();

    @Override
    public DayForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.day_forecast_item, parent, false);
        return new DayForecastViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onBindViewHolder(DayForecastViewHolder holder, int position) {
        Weather weather = mItems.get(position);
        String date = Utils.convertLongToDateString(weather.getDateTime() * 1000);
        holder.mDateTextView.setText(date);
        if(weather.getMain() != null) {
            String dayTemp = Utils.getTemperatureString(weather.getMain().getDayTemp());
            holder.mDayTemperatureTextView.setText(dayTemp);
        }
    }

    public void setItems(List<Weather> items) {
        mItems = items;
    }

    public class DayForecastViewHolder extends RecyclerView.ViewHolder {

        private TextView mDateTextView;
        private TextView mDayTextView;
        private TextView mDayTemperatureTextView;

        public DayForecastViewHolder(View itemView) {
            super(itemView);

            mDateTextView = (TextView) itemView.findViewById(R.id.date);
            mDayTextView = (TextView) itemView.findViewById(R.id.day);
            mDayTemperatureTextView = (TextView) itemView.findViewById(R.id.dayTemperature);
        }
    }
}
