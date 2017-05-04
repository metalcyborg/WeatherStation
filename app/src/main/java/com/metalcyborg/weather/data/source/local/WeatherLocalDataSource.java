package com.metalcyborg.weather.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;

import com.metalcyborg.weather.citylist.parseservice.CityData;
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
    public void addCitiesData(CityData[] data) throws SQLiteException {
        SQLiteDatabase db = null;
        try {
            db = mDatabaseHelper.getWritableDatabase();
            db.beginTransaction();

            for (CityData cityData : data) {
                ContentValues cv = new ContentValues();
                cv.put(WeatherPersistenceContract.CityTable.COLUMN_OPEN_WEATHER_ID,
                        cityData.getId());
                cv.put(WeatherPersistenceContract.CityTable.COLUMN_CITY_NAME,
                        cityData.getCityName());
                cv.put(WeatherPersistenceContract.CityTable.COLUMN_COUNTRY_NAME,
                        cityData.getCountryName());
                cv.put(WeatherPersistenceContract.CityTable.COLUMN_LONGITUDE,
                        cityData.getCoord().getLon());
                cv.put(WeatherPersistenceContract.CityTable.COLUMN_LATITUDE,
                        cityData.getCoord().getLat());
                db.insertOrThrow(WeatherPersistenceContract.CityTable.TABLE_NAME, null,
                        cv);
            }

            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
            throw new SQLiteException("City data insertion error");
        } finally {
            if(db != null) {
                db.endTransaction();
                db.close();
            }
        }
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
