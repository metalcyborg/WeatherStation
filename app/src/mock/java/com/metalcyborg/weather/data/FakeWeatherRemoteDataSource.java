package com.metalcyborg.weather.data;


import android.support.annotation.VisibleForTesting;

import com.metalcyborg.weather.data.source.remote.RemoteDataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeWeatherRemoteDataSource implements RemoteDataSource {

    private static volatile FakeWeatherRemoteDataSource mInstance;

    private static final Map<String, Weather> CURRENT_WEATHER = new HashMap<>();
    private static final Map<String, List<Weather>> FORECAST_3H = new HashMap<>();
    private static final Map<String, List<Weather>> FORECAST_DAILY = new HashMap<>();

    private FakeWeatherRemoteDataSource() {

    }

    public static FakeWeatherRemoteDataSource getInstance() {
        if(mInstance == null) {
            synchronized(FakeWeatherRemoteDataSource.class) {
                if(mInstance == null) {
                    mInstance = new FakeWeatherRemoteDataSource();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void loadWeatherData(String cityId, GetWeatherCallback callback) {
        if(CURRENT_WEATHER.containsKey(cityId)) {
            callback.onDataLoaded(cityId, CURRENT_WEATHER.get(cityId));
        } else {
            callback.onDataNotAvailable(cityId);
        }
    }

    @Override
    public void load3HForecastData(String cityId, GetForecastCallback callback) {
        if(FORECAST_3H.containsKey(cityId)) {
            callback.onDataLoaded(FORECAST_3H.get(cityId));
        } else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void load13DForecastData(String cityId, GetForecastCallback callback) {
        if(FORECAST_DAILY.containsKey(cityId)) {
            callback.onDataLoaded(FORECAST_DAILY.get(cityId));
        } else {
            callback.onDataNotAvailable();
        }
    }

    @VisibleForTesting
    public void addCurrentWeatherData(String cityId, Weather weather) {
        CURRENT_WEATHER.put(cityId, weather);
    }

    @VisibleForTesting
    public void addForecast3H(String cityId, List<Weather> forecast) {
        FORECAST_3H.put(cityId, forecast);
    }

    @VisibleForTesting
    public void addForecastDaily(String cityId, List<Weather> forecast) {
        FORECAST_DAILY.put(cityId, forecast);
    }
}
