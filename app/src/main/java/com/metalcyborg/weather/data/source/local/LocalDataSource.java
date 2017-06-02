package com.metalcyborg.weather.data.source.local;

import android.database.sqlite.SQLiteException;

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

    interface LoadForecastCallback {

        void onDataLoaded(List<Weather> forecast);

        void onDataNotAvailable();
    }

    interface GetWeatherCallback {

        void onDataLoaded(Weather weatherData);

        void onDataNotAvailable();
    }

    boolean isCitiesDataAdded();

    void setCitiesDataAdded();

    void loadWeatherData(LoadWeatherListCallback callback);

    void getWeatherByCityId(String id, GetWeatherCallback callback);

    void load3HForecastData(String cityId, LoadForecastCallback callback);

    void load13DForecastData(String cityId, LoadForecastCallback callback);

    void findCitiesByPartOfTheName(String partOfTheName, int count, FindCityListCallback callback);

    void addNewCityToChosenCityList(City city) throws SQLiteException;

    void deleteCitiesFromChosenCityList(List<City> items);

    void deleteAllCitiesAndForecastData();

    void updateCurrentWeather(String cityId, Weather weather);

    void update3HForecast(String cityId, List<Weather> forecast);

    void update13DForecast(String cityId, List<Weather> forecast);

}
