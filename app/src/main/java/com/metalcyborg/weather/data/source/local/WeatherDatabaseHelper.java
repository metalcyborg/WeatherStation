package com.metalcyborg.weather.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.metalcyborg.weather.data.Weather;

/**
 * Created by metalcyborg on 18.04.17.
 */

public class WeatherDatabaseHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Weather.db";

    private static final String COMMA = ", ";
    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_INTEGER = " INTEGER";
    private static final String TYPE_BOOLEAN = " INTEGER";
    private static final String TYPE_FLOAT = " FLOAT";


    private static final String CREATE_FTS_CITY_TABLE = "CREATE VIRTUAL TABLE" +
            WeatherPersistenceContract.FtsCityTable.TABLE_NAME + " USING fts3 (" +
            WeatherPersistenceContract.FtsCityTable._ID + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT" + COMMA +
            WeatherPersistenceContract.FtsCityTable.COLUMN_OPEN_WEATHER_ID + TYPE_TEXT + COMMA +
            WeatherPersistenceContract.FtsCityTable.COLUMN_CITY_NAME + TYPE_TEXT + COMMA +
            WeatherPersistenceContract.FtsCityTable.COLUMN_COUNTRY_NAME + TYPE_TEXT + COMMA +
            WeatherPersistenceContract.FtsCityTable.COLUMN_LONGITUDE + TYPE_FLOAT + COMMA +
            WeatherPersistenceContract.FtsCityTable.COLUMN_LATITUDE + TYPE_FLOAT +
            " )";

    private static final String CREATE_CHOSEN_CITIES_TABLE = "CREATE TABLE" +
            WeatherPersistenceContract.ChosenCitiesTable.TABLE_NAME + " (" +
            WeatherPersistenceContract.ChosenCitiesTable._ID + TYPE_TEXT + " PRIMARY KEY" + COMMA +
            WeatherPersistenceContract.ChosenCitiesTable.COLUMN_CITY_NAME + TYPE_TEXT + COMMA +
            WeatherPersistenceContract.ChosenCitiesTable.COLUMN_COUNTRY_NAME + TYPE_TEXT +
            " )";

    private static final String CREATE_WEATHER_TABLE = "CREATE TABLE" +
            WeatherPersistenceContract.WeatherTable.TABLE_NAME + " (" +
            WeatherPersistenceContract.WeatherTable._ID + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT" + COMMA +
            WeatherPersistenceContract.WeatherTable.COLUMN_CHOSEN_CITY_ID + TYPE_TEXT + COMMA +
            WeatherPersistenceContract.WeatherTable.COLUMN_DATA_RECEIVED + TYPE_BOOLEAN + COMMA +
            WeatherPersistenceContract.WeatherTable.COLUMN_DATE + TYPE_INTEGER + COMMA +
            WeatherPersistenceContract.WeatherTable.COLUMN_SUNRISE_TIME + TYPE_INTEGER + COMMA +
            WeatherPersistenceContract.WeatherTable.COLUMN_SUNSET_TIME + TYPE_INTEGER + COMMA +
            WeatherPersistenceContract.WeatherTable.COLUMN_TEMPERATURE + TYPE_FLOAT + COMMA +
            WeatherPersistenceContract.WeatherTable.COLUMN_HUMIDITY + TYPE_FLOAT + COMMA +
            WeatherPersistenceContract.WeatherTable.COLUMN_PRESSURE + TYPE_FLOAT + COMMA +
            WeatherPersistenceContract.WeatherTable.COLUMN_WIND_SPEED + TYPE_FLOAT + COMMA +
            WeatherPersistenceContract.WeatherTable.COLUMN_WIND_DIRECTION + TYPE_FLOAT + COMMA +
            WeatherPersistenceContract.WeatherTable.COLUMN_CLOUDINESS + TYPE_INTEGER + COMMA +
            WeatherPersistenceContract.WeatherTable.COLUMN_CONDITION_ID + TYPE_INTEGER + COMMA +
            WeatherPersistenceContract.WeatherTable.COLUMN_WEATHER_GROUP + TYPE_TEXT + COMMA +
            WeatherPersistenceContract.WeatherTable.COLUMN_DESCRIPTION + TYPE_TEXT + COMMA +
            WeatherPersistenceContract.WeatherTable.COLUMN_ICON + TYPE_TEXT + COMMA +
            WeatherPersistenceContract.WeatherTable.COLUMN_VOLUME_RAIN_3H + TYPE_INTEGER + COMMA +
            WeatherPersistenceContract.WeatherTable.COLUMN_VOLUME_SNOW_3H + TYPE_INTEGER +
            " )";

    public WeatherDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FTS_CITY_TABLE);
        db.execSQL(CREATE_CHOSEN_CITIES_TABLE);
        db.execSQL(CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
