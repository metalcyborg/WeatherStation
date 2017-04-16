package com.metalcyborg.weather.data;


import com.metalcyborg.weather.data.source.WeatherDataSource;

/**
 * Created by metalcyborg on 16.04.17.
 */

public class FakeWeatherRemoteDataSource implements WeatherDataSource {

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
}
