package com.metalcyborg.weather.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.source.WeatherDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by root on 14.04.17.
 */

public class WeatherLocalDataSource implements WeatherDataSource {

    private static volatile WeatherLocalDataSource mInstance;
    private Context mContext;

    private WeatherLocalDataSource(@NonNull Context context) {
        mContext = checkNotNull(context);
    }

    public static WeatherLocalDataSource getInstance(Context context) {
        if(mInstance == null) {
            synchronized (WeatherLocalDataSource.class) {
                if(mInstance == null) {
                    mInstance = new WeatherLocalDataSource(context);
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
