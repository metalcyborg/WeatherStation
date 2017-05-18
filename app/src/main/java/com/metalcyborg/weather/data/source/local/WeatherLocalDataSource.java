package com.metalcyborg.weather.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.collect.Lists;
import com.metalcyborg.weather.citylist.parseservice.CityData;
import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.CityWeather;
import com.metalcyborg.weather.data.Weather;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class WeatherLocalDataSource implements LocalDataSource {

    private static final String TAG = "WeatherLocalDataSource";
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
                "t1." + WeatherPersistenceContract.ChosenCitiesTable._ID, // 0
                WeatherPersistenceContract.ChosenCitiesTable.COLUMN_OPEN_WEATHER_ID, // 1
                WeatherPersistenceContract.ChosenCitiesTable.COLUMN_CITY_NAME, // 2
                WeatherPersistenceContract.ChosenCitiesTable.COLUMN_COUNTRY_NAME, // 3
                "t2." + WeatherPersistenceContract.WeatherTable.COLUMN_CHOSEN_CITY_ID, // 4
                WeatherPersistenceContract.WeatherTable.COLUMN_DATA_RECEIVED, // 5
                WeatherPersistenceContract.WeatherTable.COLUMN_DATE, // 6
                WeatherPersistenceContract.WeatherTable.COLUMN_SUNRISE_TIME, // 7
                WeatherPersistenceContract.WeatherTable.COLUMN_SUNSET_TIME, // 8
                WeatherPersistenceContract.WeatherTable.COLUMN_TEMPERATURE, // 9
                WeatherPersistenceContract.WeatherTable.COLUMN_PRESSURE, // 10
                WeatherPersistenceContract.WeatherTable.COLUMN_HUMIDITY, // 11
                WeatherPersistenceContract.WeatherTable.COLUMN_WIND_SPEED, // 12
                WeatherPersistenceContract.WeatherTable.COLUMN_WIND_DIRECTION, // 13
                WeatherPersistenceContract.WeatherTable.COLUMN_CLOUDINESS, // 14
                WeatherPersistenceContract.WeatherTable.COLUMN_CONDITION_ID, // 15
                WeatherPersistenceContract.WeatherTable.COLUMN_WEATHER_GROUP, // 16
                WeatherPersistenceContract.WeatherTable.COLUMN_DESCRIPTION, // 17
                WeatherPersistenceContract.WeatherTable.COLUMN_ICON, // 18
                WeatherPersistenceContract.WeatherTable.COLUMN_VOLUME_RAIN_3H, // 19
                WeatherPersistenceContract.WeatherTable.COLUMN_VOLUME_SNOW_3H // 20
        };

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(WeatherPersistenceContract.ChosenCitiesTable.TABLE_NAME + " AS t1" +
                " LEFT JOIN" + WeatherPersistenceContract.WeatherTable.TABLE_NAME + " AS t2" +
                " ON" + " (t2." + WeatherPersistenceContract.WeatherTable.COLUMN_CHOSEN_CITY_ID + " =" +
                " t1." + WeatherPersistenceContract.ChosenCitiesTable.COLUMN_OPEN_WEATHER_ID + ")");

        Cursor cursor = builder.query(mDatabaseHelper.getReadableDatabase(), columns, null, null,
                null, null, null);

        List<CityWeather> cityWeatherList = new ArrayList<>();
        while (cursor.moveToNext()) {
            City city = new City(cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    0, 0);
            Weather weather = null;
            if (cursor.getInt(5) == 1) {
                // Data was received
                weather = new Weather(cursor.getLong(6));

                Weather.WeatherDescription description = new Weather.WeatherDescription(
                        cursor.getInt(15), cursor.getString(16), cursor.getString(17),
                        cursor.getString(18)
                );
                Weather.Main main = new Weather.Main(cursor.getFloat(9), cursor.getFloat(10),
                        cursor.getFloat(11));
                Weather.Wind wind = new Weather.Wind(cursor.getFloat(12), cursor.getFloat(13));
                Weather.Clouds clouds = new Weather.Clouds(cursor.getInt(14));
                Weather.Sys sys = new Weather.Sys(cursor.getLong(7), cursor.getLong(8));
                if(!cursor.isNull(19)) {
                    Weather.Rain rain = new Weather.Rain(cursor.getFloat(19));
                    weather.setRain(rain);
                }

                if(!cursor.isNull(20)) {
                    Weather.Snow snow = new Weather.Snow(cursor.getFloat(20));
                    weather.setSnow(snow);
                }

                weather.setWeatherDescription(description);
                weather.setMain(main);
                weather.setWind(wind);
                weather.setClouds(clouds);
                weather.setSys(sys);

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
    public void load3HForecastData(String cityId, LoadForecastCallback callback) {

    }

    @Override
    public void load13DForecastData(String cityId, LoadForecastCallback callback) {

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
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        Cursor chosenCityCursor = null;

        try {
            chosenCityCursor = db.query(
                    WeatherPersistenceContract.ChosenCitiesTable.TABLE_NAME,
                    new String[]{WeatherPersistenceContract.ChosenCitiesTable._ID,
                    WeatherPersistenceContract.ChosenCitiesTable.COLUMN_OPEN_WEATHER_ID},
                    WeatherPersistenceContract.ChosenCitiesTable.COLUMN_OPEN_WEATHER_ID + "=?",
                    new String[]{city.getOpenWeatherId()}, null, null, null);
            if (chosenCityCursor.getCount() > 0) {
                Log.d(TAG, "City " + city.getName() + " is already added");
                return;
            }

            db.beginTransaction();

            // Add data to the ChosenCityTable
            ContentValues cv = new ContentValues();
            cv.put(WeatherPersistenceContract.ChosenCitiesTable.COLUMN_OPEN_WEATHER_ID,
                    city.getOpenWeatherId());
            cv.put(WeatherPersistenceContract.ChosenCitiesTable.COLUMN_CITY_NAME,
                    city.getName());
            cv.put(WeatherPersistenceContract.ChosenCitiesTable.COLUMN_COUNTRY_NAME,
                    city.getCountry());
            db.insertOrThrow(WeatherPersistenceContract.ChosenCitiesTable.TABLE_NAME, null, cv);

            // Add default data to the Weather table
            cv.clear();
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_CHOSEN_CITY_ID,
                    city.getOpenWeatherId());
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_DATA_RECEIVED, 0);// False value
            db.insertOrThrow(WeatherPersistenceContract.WeatherTable.TABLE_NAME, null, cv);

            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
            throw new SQLiteException("Error of insertion to the ChosenCities table");
        } finally {
            if (db.inTransaction()) {
                db.endTransaction();
            }
            if (chosenCityCursor != null) {
                chosenCityCursor.close();
            }
            db.close();
        }
    }

    private ContentValues generateContentValues(Weather weather) {
        ContentValues cv = new ContentValues();
        cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_DATE,
                weather.getDateTime());
        cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_SUNRISE_TIME,
                weather.getSys().getSunrise());
        cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_SUNSET_TIME,
                weather.getSys().getSunset());
        cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_TEMPERATURE,
                weather.getMain().getTemp());
        cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_PRESSURE,
                weather.getMain().getPressure());
        cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_HUMIDITY,
                weather.getMain().getHumidity());
        cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_WIND_SPEED,
                weather.getWind().getSpeed());
        cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_WIND_DIRECTION,
                weather.getWind().getDeg());
        cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_CLOUDINESS,
                weather.getClouds().getCloudiness());
        cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_CONDITION_ID,
                weather.getWeatherDescription().getId());
        cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_WEATHER_GROUP,
                weather.getWeatherDescription().getMain());
        cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_DESCRIPTION,
                weather.getWeatherDescription().getDetail());
        cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_ICON,
                weather.getWeatherDescription().getIcon());
        if(weather.getRain() != null) {
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_VOLUME_RAIN_3H,
                    weather.getRain().getVolume3H());
        }

        if(weather.getSnow() != null) {
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_VOLUME_SNOW_3H,
                    weather.getSnow().getVolume3H());
        }

        return cv;
    }

    @Override
    public void updateWeather(String cityId, Weather weather) {
        SQLiteDatabase db = null;
        try {
            db = mDatabaseHelper.getWritableDatabase();

            ContentValues cv = generateContentValues(weather);
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_DATA_RECEIVED, 1); // True value

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

    @Override
    public void update3HForecast(String cityId, List<Weather> forecast) {

    }

    @Override
    public void update13DForecast(String cityId, List<Weather> forecast) {

    }
}
