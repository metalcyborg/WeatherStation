package com.metalcyborg.weather.data.source.remote;

import com.metalcyborg.weather.data.Weather;

import java.util.List;

public interface RemoteDataSource {

    interface GetWeatherCallback {

        void onDataLoaded(String cityId, Weather weather);

        void onDataNotAvailable(String cityId);
    }

    interface GetForecastCallback {
        void onDataLoaded(List<Weather> forecast);

        void onDataNotAvailable();
    }

    void loadWeatherData(String cityId, GetWeatherCallback callback);

    void load3HForecastData(String cityId, GetForecastCallback callback);

    void load13DForecastData(String cityId, GetForecastCallback callback);
}
