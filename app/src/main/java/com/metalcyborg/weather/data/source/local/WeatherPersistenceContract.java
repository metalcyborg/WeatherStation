package com.metalcyborg.weather.data.source.local;

import android.provider.BaseColumns;

public class WeatherPersistenceContract {

    public WeatherPersistenceContract() {

    }

    public static class CityTable implements BaseColumns {
        public static final String TABLE_NAME = " City";
        public static final String COLUMN_OPEN_WEATHER_ID = " OpenWeatherId";
        public static final String COLUMN_CITY_NAME = " CityName";
        public static final String COLUMN_COUNTRY_NAME = " CountryName";
        public static final String COLUMN_LONGITUDE = " Longitude";
        public static final String COLUMN_LATITUDE = " Latitude";
    }
}
