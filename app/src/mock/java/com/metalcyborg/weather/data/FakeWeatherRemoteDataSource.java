package com.metalcyborg.weather.data;


import com.metalcyborg.weather.data.source.WeatherDataSource;
import com.metalcyborg.weather.data.source.remote.RemoteDataSource;

/**
 * Created by metalcyborg on 16.04.17.
 */

public class FakeWeatherRemoteDataSource implements RemoteDataSource {

    private static volatile FakeWeatherRemoteDataSource mInstance;

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
    public void loadWeatherData(WeatherDataSource.LoadWeatherListCallback callback) {

    }
}
