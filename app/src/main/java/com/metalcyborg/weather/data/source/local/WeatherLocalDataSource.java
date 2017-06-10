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

    private Weather generateWeatherObject(Cursor cursor) {
        Weather weather = new Weather(cursor.getLong(
                cursor.getColumnIndex(WeatherPersistenceContract.WeatherTable.COLUMN_DATE.trim())));

        Weather.WeatherDescription description = new Weather.WeatherDescription(
                cursor.getInt(cursor.getColumnIndex(WeatherPersistenceContract.WeatherTable.COLUMN_CONDITION_ID.trim())),
                cursor.getString(cursor.getColumnIndex(WeatherPersistenceContract.WeatherTable.COLUMN_WEATHER_GROUP.trim())),
                cursor.getString(cursor.getColumnIndex(WeatherPersistenceContract.WeatherTable.COLUMN_DESCRIPTION.trim())),
                cursor.getString(cursor.getColumnIndex(WeatherPersistenceContract.WeatherTable.COLUMN_ICON.trim()))
        );
        Weather.Main main = new Weather.Main(
                cursor.getFloat(cursor.getColumnIndex(WeatherPersistenceContract.WeatherTable.COLUMN_TEMPERATURE_DAY.trim())),
                cursor.getFloat(cursor.getColumnIndex(WeatherPersistenceContract.WeatherTable.COLUMN_TEMPERATURE_NIGHT.trim())),
                cursor.getFloat(cursor.getColumnIndex(WeatherPersistenceContract.WeatherTable.COLUMN_PRESSURE.trim())),
                cursor.getFloat(cursor.getColumnIndex(WeatherPersistenceContract.WeatherTable.COLUMN_HUMIDITY.trim())));
        Weather.Wind wind = new Weather.Wind(
                cursor.getFloat(cursor.getColumnIndex(WeatherPersistenceContract.WeatherTable.COLUMN_WIND_SPEED.trim())),
                cursor.getFloat(cursor.getColumnIndex(WeatherPersistenceContract.WeatherTable.COLUMN_WIND_DIRECTION.trim())));
        Weather.Clouds clouds = new Weather.Clouds(
                cursor.getInt(cursor.getColumnIndex(WeatherPersistenceContract.WeatherTable.COLUMN_CLOUDINESS.trim())));
        Weather.Sys sys = new Weather.Sys(
                cursor.getLong(cursor.getColumnIndex(WeatherPersistenceContract.WeatherTable.COLUMN_SUNRISE_TIME.trim())),
                cursor.getLong(cursor.getColumnIndex(WeatherPersistenceContract.WeatherTable.COLUMN_SUNSET_TIME.trim())));
        if (!cursor.isNull(cursor.getColumnIndex(WeatherPersistenceContract.WeatherTable.COLUMN_VOLUME_RAIN_3H.trim()))) {
            Weather.Rain rain = new Weather.Rain(
                    cursor.getFloat(cursor.getColumnIndex(WeatherPersistenceContract.WeatherTable.COLUMN_VOLUME_RAIN_3H.trim())));
            weather.setRain(rain);
        }

        if (!cursor.isNull(cursor.getColumnIndex(WeatherPersistenceContract.WeatherTable.COLUMN_VOLUME_SNOW_3H.trim()))) {
            Weather.Snow snow = new Weather.Snow(
                    cursor.getFloat(cursor.getColumnIndex(WeatherPersistenceContract.WeatherTable.COLUMN_VOLUME_SNOW_3H.trim())));
            weather.setSnow(snow);
        }

        weather.setWeatherDescription(description);
        weather.setMain(main);
        weather.setWind(wind);
        weather.setClouds(clouds);
        weather.setSys(sys);

        return weather;
    }

    @Override
    public void loadWeatherData(LoadWeatherListCallback callback) {
        String[] columns = new String[]{
                "t1." + WeatherPersistenceContract.ChosenCitiesTable._ID,
                WeatherPersistenceContract.ChosenCitiesTable.COLUMN_OPEN_WEATHER_ID,
                WeatherPersistenceContract.ChosenCitiesTable.COLUMN_CITY_NAME,
                WeatherPersistenceContract.ChosenCitiesTable.COLUMN_COUNTRY_NAME,
                WeatherPersistenceContract.ChosenCitiesTable.COLUMN_LONGITUDE,
                WeatherPersistenceContract.ChosenCitiesTable.COLUMN_LATITUDE,
                WeatherPersistenceContract.ChosenCitiesTable.COLUMN_TIMEZONE,
                "t2." + WeatherPersistenceContract.WeatherTable.COLUMN_CHOSEN_CITY_ID,
                WeatherPersistenceContract.WeatherTable.COLUMN_DATA_RECEIVED,
                WeatherPersistenceContract.WeatherTable.COLUMN_DATE,
                WeatherPersistenceContract.WeatherTable.COLUMN_SUNRISE_TIME,
                WeatherPersistenceContract.WeatherTable.COLUMN_SUNSET_TIME,
                WeatherPersistenceContract.WeatherTable.COLUMN_TEMPERATURE_DAY,
                WeatherPersistenceContract.WeatherTable.COLUMN_TEMPERATURE_NIGHT,
                WeatherPersistenceContract.WeatherTable.COLUMN_PRESSURE,
                WeatherPersistenceContract.WeatherTable.COLUMN_HUMIDITY,
                WeatherPersistenceContract.WeatherTable.COLUMN_WIND_SPEED,
                WeatherPersistenceContract.WeatherTable.COLUMN_WIND_DIRECTION,
                WeatherPersistenceContract.WeatherTable.COLUMN_CLOUDINESS,
                WeatherPersistenceContract.WeatherTable.COLUMN_CONDITION_ID,
                WeatherPersistenceContract.WeatherTable.COLUMN_WEATHER_GROUP,
                WeatherPersistenceContract.WeatherTable.COLUMN_DESCRIPTION,
                WeatherPersistenceContract.WeatherTable.COLUMN_ICON,
                WeatherPersistenceContract.WeatherTable.COLUMN_VOLUME_RAIN_3H,
                WeatherPersistenceContract.WeatherTable.COLUMN_VOLUME_SNOW_3H,
                WeatherPersistenceContract.WeatherTable.COLUMN_FORECAST
        };

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(WeatherPersistenceContract.ChosenCitiesTable.TABLE_NAME + " AS t1" +
                " LEFT JOIN" + WeatherPersistenceContract.WeatherTable.TABLE_NAME + " AS t2" +
                " ON" + " (t2." + WeatherPersistenceContract.WeatherTable.COLUMN_CHOSEN_CITY_ID + " =" +
                " t1." + WeatherPersistenceContract.ChosenCitiesTable.COLUMN_OPEN_WEATHER_ID + ")");

        Cursor cursor = builder.query(mDatabaseHelper.getReadableDatabase(), columns,
                WeatherPersistenceContract.WeatherTable.COLUMN_FORECAST + " = ?", new String[]{"0"},
                null, null, null);

        List<CityWeather> cityWeatherList = new ArrayList<>();
        while (cursor.moveToNext()) {
            City city = new City(
                    cursor.getString(cursor.getColumnIndex(WeatherPersistenceContract.ChosenCitiesTable.COLUMN_OPEN_WEATHER_ID.trim())),
                    cursor.getString(cursor.getColumnIndex(WeatherPersistenceContract.ChosenCitiesTable.COLUMN_CITY_NAME.trim())),
                    cursor.getString(cursor.getColumnIndex(WeatherPersistenceContract.ChosenCitiesTable.COLUMN_COUNTRY_NAME.trim())),
                    cursor.getFloat(cursor.getColumnIndex(WeatherPersistenceContract.ChosenCitiesTable.COLUMN_LATITUDE.trim())),
                    cursor.getFloat(cursor.getColumnIndex(WeatherPersistenceContract.ChosenCitiesTable.COLUMN_LONGITUDE.trim())),
                    cursor.getString(cursor.getColumnIndex(WeatherPersistenceContract.ChosenCitiesTable.COLUMN_TIMEZONE.trim()))
            );
            Weather weather = null;
            if (cursor.getInt(cursor.getColumnIndex(WeatherPersistenceContract.WeatherTable.COLUMN_DATA_RECEIVED.trim())) == 1) {
                // Data was received
                weather = generateWeatherObject(cursor);
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
    public void getWeatherByCityId(String id, GetWeatherCallback callback) {
        String[] columns = new String[] {
                WeatherPersistenceContract.WeatherTable.COLUMN_CHOSEN_CITY_ID,
                WeatherPersistenceContract.WeatherTable.COLUMN_DATA_RECEIVED,
                WeatherPersistenceContract.WeatherTable.COLUMN_DATE,
                WeatherPersistenceContract.WeatherTable.COLUMN_SUNRISE_TIME,
                WeatherPersistenceContract.WeatherTable.COLUMN_SUNSET_TIME,
                WeatherPersistenceContract.WeatherTable.COLUMN_TEMPERATURE_DAY,
                WeatherPersistenceContract.WeatherTable.COLUMN_TEMPERATURE_NIGHT,
                WeatherPersistenceContract.WeatherTable.COLUMN_PRESSURE,
                WeatherPersistenceContract.WeatherTable.COLUMN_HUMIDITY,
                WeatherPersistenceContract.WeatherTable.COLUMN_WIND_SPEED,
                WeatherPersistenceContract.WeatherTable.COLUMN_WIND_DIRECTION,
                WeatherPersistenceContract.WeatherTable.COLUMN_CLOUDINESS,
                WeatherPersistenceContract.WeatherTable.COLUMN_CONDITION_ID,
                WeatherPersistenceContract.WeatherTable.COLUMN_WEATHER_GROUP,
                WeatherPersistenceContract.WeatherTable.COLUMN_DESCRIPTION,
                WeatherPersistenceContract.WeatherTable.COLUMN_ICON,
                WeatherPersistenceContract.WeatherTable.COLUMN_VOLUME_RAIN_3H,
                WeatherPersistenceContract.WeatherTable.COLUMN_VOLUME_SNOW_3H,
                WeatherPersistenceContract.WeatherTable.COLUMN_FORECAST
        };

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(WeatherPersistenceContract.WeatherTable.TABLE_NAME);

        Cursor cursor = builder.query(mDatabaseHelper.getReadableDatabase(), columns,
                WeatherPersistenceContract.WeatherTable.COLUMN_CHOSEN_CITY_ID + "=?",
                new String[] {id}, null, null, null);

        Weather weather = null;
        if (cursor.moveToFirst()) {
            if (cursor.getInt(cursor.getColumnIndex(
                    WeatherPersistenceContract.WeatherTable.COLUMN_DATA_RECEIVED.trim())) == 1) {
                // Data was received
                weather = generateWeatherObject(cursor);
            }
        }

        if (weather == null) {
            callback.onDataNotAvailable();
        } else {
            callback.onDataLoaded(weather);
        }
    }

    private void loadForecast(String cityId, boolean forecast3H, LoadForecastCallback callback) {
        String selection = WeatherPersistenceContract.WeatherTable.COLUMN_CHOSEN_CITY_ID + "=?" +
                " AND" + WeatherPersistenceContract.WeatherTable.COLUMN_FORECAST + "=?" +
                " AND" + WeatherPersistenceContract.WeatherTable.COLUMN_FORECAST_3H + "=?";
        String[] selectionArgs = new String[]{cityId, "1", forecast3H ? "1" : "0"};

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(WeatherPersistenceContract.WeatherTable.TABLE_NAME);
        Cursor cursor = builder.query(mDatabaseHelper.getReadableDatabase(), null,
                selection, selectionArgs, null, null, null);

        List<Weather> weatherList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Weather weather = generateWeatherObject(cursor);
            weatherList.add(weather);
        }

        cursor.close();

        if(weatherList.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            callback.onDataLoaded(weatherList);
        }
    }

    @Override
    public void load3HForecastData(String cityId, LoadForecastCallback callback) {
        loadForecast(cityId, true, callback);
    }

    @Override
    public void load13DForecastData(String cityId, LoadForecastCallback callback) {
        loadForecast(cityId, false, callback);
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
                    cursor.getFloat(4), cursor.getFloat(5), null);
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
            cv.put(WeatherPersistenceContract.ChosenCitiesTable.COLUMN_LATITUDE,
                    city.getLatitude());
            cv.put(WeatherPersistenceContract.ChosenCitiesTable.COLUMN_LONGITUDE,
                    city.getLongitude());
            db.insertOrThrow(WeatherPersistenceContract.ChosenCitiesTable.TABLE_NAME, null, cv);

            // Add default data to the Weather table
            cv.clear();
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_CHOSEN_CITY_ID,
                    city.getOpenWeatherId());
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_DATA_RECEIVED, 0); // False value
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_FORECAST, 0); // False value
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_FORECAST_3H, 0); // False value
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

    @Override
    public void deleteCitiesFromChosenCityList(List<City> items) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        try {
            db.beginTransaction();

            for (City city : items) {
                // Delete from chosen city table
                db.delete(WeatherPersistenceContract.ChosenCitiesTable.TABLE_NAME,
                        WeatherPersistenceContract.ChosenCitiesTable.COLUMN_OPEN_WEATHER_ID + "=?",
                        new String[]{city.getOpenWeatherId()});

                // Delete from weather table
                db.delete(WeatherPersistenceContract.WeatherTable.TABLE_NAME,
                        WeatherPersistenceContract.WeatherTable.COLUMN_CHOSEN_CITY_ID + "=?",
                        new String[]{city.getOpenWeatherId()});
            }

            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            db.close();
        }
    }

    @Override
    public void deleteAllCitiesAndForecastData() {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        try {
            db.beginTransaction();

            // Delete all data from chosen city table
            db.delete(WeatherPersistenceContract.ChosenCitiesTable.TABLE_NAME, null, null);

            // Delete all data from weather table
            db.delete(WeatherPersistenceContract.WeatherTable.TABLE_NAME, null, null);

            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private ContentValues generateContentValues(Weather weather) {
        ContentValues cv = new ContentValues();
        cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_DATE,
                weather.getDateTime());
        // Sys
        if (weather.getSys() != null) {
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_SUNRISE_TIME,
                    weather.getSys().getSunrise());
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_SUNSET_TIME,
                    weather.getSys().getSunset());
        }
        if (weather.getMain() != null) {
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_TEMPERATURE_DAY,
                    weather.getMain().getDayTemp());
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_TEMPERATURE_NIGHT,
                    weather.getMain().getNightTemp());
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_PRESSURE,
                    weather.getMain().getPressure());
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_HUMIDITY,
                    weather.getMain().getHumidity());
        }
        if (weather.getWind() != null) {
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_WIND_SPEED,
                    weather.getWind().getSpeed());
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_WIND_DIRECTION,
                    weather.getWind().getDeg());
        }
        if (weather.getClouds() != null) {
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_CLOUDINESS,
                    weather.getClouds().getCloudiness());
        }
        if (weather.getWeatherDescription() != null) {
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_CONDITION_ID,
                    weather.getWeatherDescription().getId());
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_WEATHER_GROUP,
                    weather.getWeatherDescription().getMain());
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_DESCRIPTION,
                    weather.getWeatherDescription().getDetail());
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_ICON,
                    weather.getWeatherDescription().getIcon());
        }
        if (weather.getRain() != null) {
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_VOLUME_RAIN_3H,
                    weather.getRain().getVolume3H());
        }

        if (weather.getSnow() != null) {
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_VOLUME_SNOW_3H,
                    weather.getSnow().getVolume3H());
        }

        return cv;
    }

    @Override
    public void updateCurrentWeather(String cityId, Weather weather) {
        SQLiteDatabase db = null;
        try {
            db = mDatabaseHelper.getWritableDatabase();

            ContentValues cv = generateContentValues(weather);
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_DATA_RECEIVED, 1); // True value
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_FORECAST, 0); // False value
            cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_FORECAST_3H, 0); // False value

            String whereClause = WeatherPersistenceContract.WeatherTable.COLUMN_FORECAST + "=? AND" +
                    WeatherPersistenceContract.WeatherTable.COLUMN_CHOSEN_CITY_ID + "=?";

            db.update(WeatherPersistenceContract.WeatherTable.TABLE_NAME, cv,
                    whereClause, new String[]{"0", cityId});
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
        updateForecast(cityId, forecast, true);
    }

    @Override
    public void update13DForecast(String cityId, List<Weather> forecast) {
        updateForecast(cityId, forecast, false);
    }

    @Override
    public void updateTimeZone(String cityId, String timeZone) {

    }

    private void updateForecast(String cityId, List<Weather> forecast, boolean forecast3H) {
        SQLiteDatabase db = null;
        try {
            db = mDatabaseHelper.getWritableDatabase();
            db.beginTransaction();

            // Delete old forecast data
            String whereClause = WeatherPersistenceContract.WeatherTable.COLUMN_FORECAST + "=? AND" +
                    WeatherPersistenceContract.WeatherTable.COLUMN_FORECAST_3H + "=? AND" +
                    WeatherPersistenceContract.WeatherTable.COLUMN_CHOSEN_CITY_ID + "=?";

            db.delete(WeatherPersistenceContract.WeatherTable.TABLE_NAME, whereClause,
                    new String[]{"1", forecast3H ? "1" : "0", cityId});

            // Add new forecast data
            for (Weather weather : forecast) {
                ContentValues cv = generateContentValues(weather);
                cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_CHOSEN_CITY_ID, cityId);
                cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_DATA_RECEIVED, 1); // True value
                cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_FORECAST, 1); // True value
                cv.put(WeatherPersistenceContract.WeatherTable.COLUMN_FORECAST_3H, forecast3H ? 1 : 0);

                db.insertOrThrow(WeatherPersistenceContract.WeatherTable.TABLE_NAME, null, cv);
            }

            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
            throw new SQLiteException("Error of inserting to the Weather table");
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }
}
