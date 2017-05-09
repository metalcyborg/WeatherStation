package com.metalcyborg.weather.data.source.remote;

import com.metalcyborg.weather.data.source.WeatherDataSource;

public class WeatherRemoteDataSource implements RemoteDataSource {

    private static volatile WeatherRemoteDataSource mInstance;

    private WeatherRemoteDataSource() {

    }

    public static WeatherRemoteDataSource getInstance() {
        if(mInstance == null) {
            synchronized (WeatherRemoteDataSource.class) {
                if(mInstance == null) {
                    mInstance = new WeatherRemoteDataSource();
                }
            }
        }

        return mInstance;
    }

    @Override
    public void loadWeatherData(WeatherDataSource.LoadWeatherListCallback callback) {

    }
}
