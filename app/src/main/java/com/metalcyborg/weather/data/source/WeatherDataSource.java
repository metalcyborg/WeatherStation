package com.metalcyborg.weather.data.source;

import com.metalcyborg.weather.data.Weather;

import java.util.List;

/**
 * Created by root on 14.04.17.
 */

public interface WeatherDataSource {

    interface LoadCityDataCallback {

        void onDataLoaded();

        void onError();
    }

    interface LoadWeatherListCallback {

        void onDataLoaded(List<Weather> weatherData);

        void onError();
    }

    interface GetWeatherCallback {

        void onDataLoaded(Weather weatherData);

        void onError();
    }

    boolean isCityDataLoaded();

    void loadCityData(LoadCityDataCallback callback);

    void loadWeatherData(LoadWeatherListCallback callback);
}
