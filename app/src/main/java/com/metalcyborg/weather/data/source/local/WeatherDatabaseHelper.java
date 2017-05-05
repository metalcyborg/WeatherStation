package com.metalcyborg.weather.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
            WeatherPersistenceContract.FtsCityTable._ID + TYPE_TEXT + " PRIMARY KEY" + COMMA +
            WeatherPersistenceContract.FtsCityTable.COLUMN_OPEN_WEATHER_ID + TYPE_TEXT + COMMA +
            WeatherPersistenceContract.FtsCityTable.COLUMN_CITY_NAME + TYPE_TEXT + COMMA +
            WeatherPersistenceContract.FtsCityTable.COLUMN_COUNTRY_NAME + TYPE_TEXT + COMMA +
            WeatherPersistenceContract.FtsCityTable.COLUMN_LONGITUDE + TYPE_FLOAT + COMMA +
            WeatherPersistenceContract.FtsCityTable.COLUMN_LATITUDE + TYPE_FLOAT +
            " )";

    public WeatherDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FTS_CITY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
