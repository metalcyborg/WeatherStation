package com.metalcyborg.weather.data.source.local;

import android.database.sqlite.SQLiteException;

import com.metalcyborg.weather.citylist.parseservice.CityData;
import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.CityWeather;
import com.metalcyborg.weather.data.Weather;

import java.util.List;

public interface LocalDataSource {

    interface LoadCityDataCallback {

        void onDataLoaded();

        void onError();
    }

    interface FindCityListCallback {
        void onDataFound(List<City> cityList);
    }

    interface LoadWeatherListCallback {

        void onDataLoaded(List<CityWeather> weatherData);

        void onDataNotAvailable();
    }

    interface GetWeatherCallback {

        void onDataLoaded(Weather weatherData);

        void onError();
    }

    boolean isCitiesDataAdded();

    void setCitiesDataAdded();

    void addCitiesData(CityData[] data) throws SQLiteException;

    void loadWeatherData(LoadWeatherListCallback callback);

    void findCitiesByPartOfTheName(String partOfTheName, int count, FindCityListCallback callback);

    void addNewCityToChosenCityList(City city) throws SQLiteException;

    void updateWeather(String cityId, Weather weather);
}
