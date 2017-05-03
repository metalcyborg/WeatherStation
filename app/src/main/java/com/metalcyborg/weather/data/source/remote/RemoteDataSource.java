package com.metalcyborg.weather.data.source.remote;

import com.metalcyborg.weather.data.source.WeatherDataSource;

/**
 * Created by metalcyborg on 03.05.17.
 */

public interface RemoteDataSource {

    void loadWeatherData(WeatherDataSource.LoadWeatherListCallback callback);
}
