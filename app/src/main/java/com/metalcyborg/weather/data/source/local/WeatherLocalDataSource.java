package com.metalcyborg.weather.data.source.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.source.WeatherDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by root on 14.04.17.
 */

public class WeatherLocalDataSource implements WeatherDataSource {

    private static final String NAME = "com.metalcyborg.weather.SharedPreferences";
    private static final String KEY_CITIES_DATA_ADDED = "com.metalcyborg.weather.key.CitiesDataAdded";
    private static volatile WeatherLocalDataSource mInstance;

    private Context mContext;
    private WeatherDatabaseHelper mDatabaseHelper;
    private SharedPreferences mSharedPreferences;

    private WeatherLocalDataSource(@NonNull Context context) {
        mContext = checkNotNull(context);
        mDatabaseHelper = new WeatherDatabaseHelper(context);
        mSharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
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
        return mSharedPreferences.getBoolean(KEY_CITIES_DATA_ADDED, false);
    }

    @Override
    public void setCitiesDataAdded() {
        mSharedPreferences.edit()
                .putBoolean(KEY_CITIES_DATA_ADDED, true)
                .apply();
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
