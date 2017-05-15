package com.metalcyborg.weather.citylist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.metalcyborg.weather.R;
import com.metalcyborg.weather.data.CityWeather;
import com.metalcyborg.weather.data.Weather;

import java.util.ArrayList;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private RecyclerView mRecyclerView;
    private WeatherClickListener mClickListener;
    private List<CityWeather> mItems = new ArrayList<>();

    public interface WeatherClickListener {
        void onClick(CityWeather cityWeather);
    }

    public WeatherAdapter(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_item, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        CityWeather cityWeather = mItems.get(position);
        holder.mCityName.setText(cityWeather.getCity().getName());
        holder.mCountryName.setText(cityWeather.getCity().getCountry());
        if(cityWeather.getWeather() != null) {
            int temp = (int) cityWeather.getWeather().getMain().getTemp();
            String tempStr = String.valueOf(temp);
            if(temp > 0) {
                tempStr = "+" + tempStr;
            } else
                if(temp < 0) {
                    tempStr = "-" + tempStr;
                }
            holder.mTemperature.setText(tempStr);
        }
    }

    public void setItems(List<CityWeather> items) {
        mItems = items;
    }

    public void updateItem(String cityId, Weather weather) {
        int position = getPosition(cityId);
        if(position != -1) {
            mItems.get(position).setWeather(weather);
            notifyItemChanged(position);
        }
    }

    private int getPosition(String cityId) {
        int position = -1;

        int i = 0;
        while(position == -1 && i < mItems.size()) {
            if(mItems.get(i).getCity().getOpenWeatherId().equals(cityId)) {
                position = i;
            }
            ++i;
        }

        return position;
    }

    public void setOnItemClickListener(WeatherClickListener listener) {
        mClickListener = listener;
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mCityName;
        private TextView mCountryName;
        private TextView mTemperature;

        public WeatherViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mCityName = (TextView) itemView.findViewById(R.id.cityName);
            mCountryName = (TextView) itemView.findViewById(R.id.countryName);
            mTemperature = (TextView) itemView.findViewById(R.id.temperature);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                int position = mRecyclerView.getLayoutManager().getPosition(v);
                mClickListener.onClick(mItems.get(position));
            }
        }
    }
}
