package com.metalcyborg.weather.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.NonNull;

import com.metalcyborg.weather.citylist.parseservice.CityData;
import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.CityWeather;
import com.metalcyborg.weather.data.Weather;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class WeatherLocalDataSource implements LocalDataSource {

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
        if (mInstance == null) {
            synchronized (WeatherLocalDataSource.class) {
                if (mInstance == null) {
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
                cv.put(WeatherPersistenceContract.FtsCityTable.COLUMN_OPEN_WEATHER_ID,
                        cityData.getId());
                cv.put(WeatherPersistenceContract.FtsCityTable.COLUMN_CITY_NAME,
                        cityData.getCityName());
                cv.put(WeatherPersistenceContract.FtsCityTable.COLUMN_COUNTRY_NAME,
                        cityData.getCountryName());
                cv.put(WeatherPersistenceContract.FtsCityTable.COLUMN_LONGITUDE,
                        cityData.getCoord().getLon());
                cv.put(WeatherPersistenceContract.FtsCityTable.COLUMN_LATITUDE,
                        cityData.getCoord().getLat());
                db.insertOrThrow(WeatherPersistenceContract.FtsCityTable.TABLE_NAME, null,
                        cv);
            }

            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
            throw new SQLiteException("City data insertion error");
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    @Override
    public void loadWeatherData(LoadWeatherListCallback callback) {
        String[] columns = new String[]{
                "t1." + WeatherPersistenceContract.ChosenCitiesTable._ID,
                WeatherPersistenceContract.ChosenCitiesTable.COLUMN_OPEN_WEATHER_ID,
                WeatherPersistenceContract.ChosenCitiesTable.COLUMN_CITY_NAME,
                WeatherPersistenceContract.ChosenCitiesTable.COLUMN_COUNTRY_NAME,
                "t2." + WeatherPersistenceContract.WeatherTable.COLUMN_CHOSEN_CITY_ID,
                WeatherPersistenceContract.WeatherTable.COLUMN_TEMPERATURE
        };

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(WeatherPersistenceContract.ChosenCitiesTable.TABLE_NAME + " AS t1" +
                " LEFT JOIN" + WeatherPersistenceContract.WeatherTable.TABLE_NAME + " AS t2" +
                " ON" + " (t2." + WeatherPersistenceContract.WeatherTable.COLUMN_CHOSEN_CITY_ID + " =" +
                " t1." + WeatherPersistenceContract.ChosenCitiesTable._ID + ")");

        Cursor cursor = builder.query(mDatabaseHelper.getReadableDatabase(), columns, null, null,
                null, null, null);

        List<CityWeather> cityWeatherList = new ArrayList<>();
        while (cursor.moveToNext()) {
            City city = new City(cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    0, 0);
            Weather weather = null;
            if (!cursor.isNull(4)) {
                weather = new Weather();
                weather.setTemperature(cursor.getFloat(4));
            }
            CityWeather cityWeather = new CityWeather(city, weather);
            cityWeatherList.add(cityWeather);
        }

        if (cityWeatherList.size() == 0) {
            callback.onDataNotAvailable();
        } else {
            callback.onDataLoaded(cityWeatherList);
        }
    }

    @Override
    public void findCitiesByPartOfTheName(String partOfTheName, int count,
                                          FindCityListCallback callback) {
        String selection = WeatherPersistenceContract.FtsCityTable.COLUMN_CITY_NAME + " MATCH ?";
        String[] selectionArgs = new String[]{partOfTheName + "*"};
        String[] columns = new String[]{
                WeatherPersistenceContract.FtsCityTable._ID,
                WeatherPersistenceContract.FtsCityTable.COLUMN_OPEN_WEATHER_ID,
                WeatherPersistenceContract.FtsCityTable.COLUMN_CITY_NAME,
                WeatherPersistenceContract.FtsCityTable.COLUMN_COUNTRY_NAME,
                WeatherPersistenceContract.FtsCityTable.COLUMN_LONGITUDE,
                WeatherPersistenceContract.FtsCityTable.COLUMN_LATITUDE
        };

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(WeatherPersistenceContract.FtsCityTable.TABLE_NAME);

        Cursor cursor = builder.query(mDatabaseHelper.getReadableDatabase(), columns,
                selection, selectionArgs, null, null, null, "" + count);

        List<City> cityList = new ArrayList<>();

        while (cursor.moveToNext()) {
            City city = new City(cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getLong(4), cursor.getLong(5)
            );
            cityList.add(city);
        }

        callback.onDataFound(cityList);

        cursor.close();
    }

    @Override
    public void addNewCityToChosenCityList(City city) throws SQLiteException {
        // TODO: check if city was already added
        SQLiteDatabase db = null;
        try {
            db = mDatabaseHelper.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(WeatherPersistenceContract.ChosenCitiesTable.COLUMN_OPEN_WEATHER_ID,
                    city.getOpenWeatherId());
            cv.put(WeatherPersistenceContract.ChosenCitiesTable.COLUMN_CITY_NAME,
                    city.getName());
            cv.put(WeatherPersistenceContract.ChosenCitiesTable.COLUMN_COUNTRY_NAME,
                    city.getCountry());

            db.insertOrThrow(WeatherPersistenceContract.ChosenCitiesTable.TABLE_NAME, null, cv);

        } catch (SQLiteException e) {
            e.printStackTrace();
            throw new SQLiteException("Error of insertion to the ChosenCities table");
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void updateWeather(String cityId, Weather weather) {
        SQLiteDatabase db = null;
        try {
            db = mDatabaseHelper.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_TEMPERATURE,
                    weather.getTemperature());

            db.update(WeatherPersistenceContract.WeatherTable.TABLE_NAME, cv,
                    WeatherPersistenceContract.WeatherTable.COLUMN_CHOSEN_CITY_ID + " = ?",
                    new String[]{cityId});
        } catch (SQLiteException e) {
            e.printStackTrace();
            throw new SQLiteException("Error of updating the Weather table");
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}
