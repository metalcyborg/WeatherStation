package com.metalcyborg.weather.data.source;

import android.database.sqlite.SQLiteException;

import com.metalcyborg.weather.citylist.parseservice.CityData;
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

        void on3HForecastLoaded(List<Weather> forecast);

        void on3HForecastNotAvailable();

        void on13DaysForecastLoaded(List<Weather> forecast);

        void on13DaysForecastNotAvailable();
    }

    boolean isCitiesDataAdded();

    void setCitiesDataAdded();

    void addCitiesData(CityData[] data) throws SQLiteException;

    void loadWeatherData(LoadWeatherCallback callback);

    void loadForecastData(LoadForecastCallback callback);

    void findCitiesByPartOfTheName(String partOfTheName, int count, FindCityListCallback callback);

    void addNewCityToChosenCityList(City city) throws SQLiteException;
}
