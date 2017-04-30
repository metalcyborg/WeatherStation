package com.metalcyborg.weather.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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
    private WeatherDatabaseHelper mDatabaseHelper;

    private WeatherLocalDataSource(@NonNull Context context) {
        mContext = checkNotNull(context);
        mDatabaseHelper = new WeatherDatabaseHelper(context);
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
    public boolean isCitiesDataAdded() {
        return false;
    }

    @Override
    public void addCitiesData(LoadCityDataCallback callback) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        // Unzip list of cities

        // Parse list of cities


        db.close();
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
