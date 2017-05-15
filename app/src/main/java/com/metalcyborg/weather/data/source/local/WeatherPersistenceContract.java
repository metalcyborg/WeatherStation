package com.metalcyborg.weather.data.source.local;

import android.provider.BaseColumns;

public class WeatherPersistenceContract {

    public WeatherPersistenceContract() {

    }

    public static class FtsCityTable implements BaseColumns {
        public static final String TABLE_NAME = " FtsCity";
        public static final String COLUMN_OPEN_WEATHER_ID = " OpenWeatherId";
        public static final String COLUMN_CITY_NAME = " CityName";
        public static final String COLUMN_COUNTRY_NAME = " CountryName";
        public static final String COLUMN_LONGITUDE = " Longitude";
        public static final String COLUMN_LATITUDE = " Latitude";
    }

    public static class ChosenCitiesTable implements BaseColumns {
        public static final String TABLE_NAME = " ChosenCities";
        public static final String COLUMN_OPEN_WEATHER_ID = " OpenWeatherId";
        public static final String COLUMN_CITY_NAME = " CityName";
        public static final String COLUMN_COUNTRY_NAME = " CountryName";
    }

    public static class WeatherTable implements BaseColumns {
        public static final String TABLE_NAME = " Weather";
        public static final String COLUMN_CHOSEN_CITY_ID = " ChosenCityId";
        public static final String COLUMN_DATA_RECEIVED = " DataReceived";
        public static final String COLUMN_DATE = " Date";
        public static final String COLUMN_SUNRISE_TIME = " SunriseTime";
        public static final String COLUMN_SUNSET_TIME = " SunsetTime";
        public static final String COLUMN_TEMPERATURE = " Temperature";
        public static final String COLUMN_HUMIDITY = " Humidity";
        public static final String COLUMN_PRESSURE = " Pressure";
        public static final String COLUMN_WIND_SPEED = " WindSpeed";
        public static final String COLUMN_WIND_DIRECTION = " WindDirection";
        public static final String COLUMN_CLOUDINESS = " Cloudiness";
        public static final String COLUMN_CONDITION_ID = " ConditionId";
        public static final String COLUMN_WEATHER_GROUP = " WeatherGroup";
        public static final String COLUMN_DESCRIPTION = " Description";
        public static final String COLUMN_ICON = " Icon";
        public static final String COLUMN_VOLUME_RAIN_3H = " VolumeRain3h";
        public static final String COLUMN_VOLUME_SNOW_3H = " VolumeSnow3h";
    }
}
