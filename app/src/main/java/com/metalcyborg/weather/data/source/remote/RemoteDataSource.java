package com.metalcyborg.weather.data.source.remote;

import com.metalcyborg.weather.data.Weather;

/**
 * Created by metalcyborg on 03.05.17.
 */

public interface RemoteDataSource {

    interface GetWeatherCallback {

        void onDataLoaded(String cityId, Weather weather);

        void onDataNotAvailable(String cityId);
    }

    void loadWeatherData(String cityId, GetWeatherCallback callback);
}
