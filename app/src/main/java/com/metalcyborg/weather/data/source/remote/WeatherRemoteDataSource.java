package com.metalcyborg.weather.data.source.remote;

import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.source.WeatherDataSource;

/**
 * Created by root on 14.04.17.
 */

public class WeatherRemoteDataSource implements WeatherDataSource {

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
    public boolean isCitiesDataLoaded() {
        return false;
    }

    @Override
    public void addCitiesData(LoadCityDataCallback callback) {

    }

    @Override
    public void loadWeatherData(LoadWeatherListCallback callback) {

    }

    @Override
    public void findCitiesByPartOfTheName(String partOfTheName, FindCityListCallback callback) {

    }

    @Override
    public void addNewCityToWeatherList(City city) {

    }
}
