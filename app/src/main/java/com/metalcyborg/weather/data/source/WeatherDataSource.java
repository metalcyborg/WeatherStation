package com.metalcyborg.weather.data.source;

import android.database.sqlite.SQLiteException;

import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.CityWeather;
import com.metalcyborg.weather.data.Weather;

import java.util.List;

public interface WeatherDataSource {

    interface LoadCityDataCallback {

        void onDataLoaded();

        void onError();
    }

    interface FindCityListCallback {
        void onDataFound(List<City> cityList);
    }

    interface LoadWeatherCallback {

        void onDataListLoaded(List<CityWeather> weatherData);

        void onDataListNotAvailable();

        void onDataLoaded(String cityId, Weather weather);

        void onDataNotAvailable(String cityId);
    }

    interface LoadForecastCallback {

        void onDataLoaded(List<Weather> forecast);

        void onDataNotAvailable();
    }

    interface LoadTimeZoneCallback {

        void onTimeZoneLoaded(String cityId, String timeZone);

        void onDataNotAvailable();
    }

    boolean isCitiesDataAdded();

    void setCitiesDataAdded();

    void loadWeatherData(LoadWeatherCallback callback);

    void load3HForecastData(String cityId, LoadForecastCallback callback);

    void load13DForecastData(String cityId, LoadForecastCallback callback);

    void loadTimeZone(String cityId, float altitude, float longitude, LoadTimeZoneCallback callback);

    void findCitiesByPartOfTheName(String partOfTheName, int count, FindCityListCallback callback);

    void addNewCityToChosenCityList(City city) throws SQLiteException;

    void deleteCitiesFromChosenCityList(List<City> cityList);

    void deleteAllDataFromCityAndWeatherLists();
}
