package com.metalcyborg.weather.data.source;

import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.metalcyborg.weather.citylist.parseservice.CityData;
import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.source.local.LocalDataSource;
import com.metalcyborg.weather.data.CityWeather;
import com.metalcyborg.weather.data.Weather;
import com.metalcyborg.weather.data.source.remote.RemoteDataSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class WeatherRepository implements WeatherDataSource {

    private static volatile WeatherRepository mInstance;
    private LocalDataSource mLocalDataSource;
    private RemoteDataSource mRemoteDataSource;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    Map<String, CityWeather> mCachedWeather;

    private WeatherRepository(@NonNull LocalDataSource localDataSource,
                              @NonNull RemoteDataSource remoteDataSource) {
        mLocalDataSource = checkNotNull(localDataSource);
        mRemoteDataSource = checkNotNull(remoteDataSource);
    }

    public static WeatherRepository getInstance(LocalDataSource localDataSource,
                                                RemoteDataSource remoteDataSource) {
        if (mInstance == null) {
            synchronized (WeatherRepository.class) {
                if (mInstance == null) {
                    mInstance = new WeatherRepository(localDataSource, remoteDataSource);
                }
            }
        }

        return mInstance;
    }

    public static void destroyInstance() {
        mInstance = null;
    }

    @Override
    public boolean isCitiesDataAdded() {
        return mLocalDataSource.isCitiesDataAdded();
    }

    @Override
    public void setCitiesDataAdded() {
        mLocalDataSource.setCitiesDataAdded();
    }

    @Override
    public void addCitiesData(CityData[] data) {
        mLocalDataSource.addCitiesData(data);
    }

    @Override
    public void loadWeatherData(final LoadWeatherCallback callback) {
        if (mCachedWeather != null) {
            List<CityWeather> weatherList = new ArrayList<>(mCachedWeather.values());
            // Load from cache
            callback.onDataListLoaded(weatherList);
            // Check remote source
            checkWeatherServer(weatherList, callback);
        } else {
            // Load from local DB
            mLocalDataSource.loadWeatherData(new LocalDataSource.LoadWeatherListCallback() {
                @Override
                public void onDataLoaded(List<CityWeather> weatherData) {
                    // Local data found
                    callback.onDataListLoaded(weatherData);
                    refreshCache(weatherData);
                    // Check remote source
                    checkWeatherServer(weatherData, callback);
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataListNotAvailable();
                }
            });
        }
    }

    private void refreshCache(List<CityWeather> weatherList) {
        if(mCachedWeather == null) {
            mCachedWeather = new LinkedHashMap<>();
        }
        mCachedWeather.clear();
        for(CityWeather cityWeather : weatherList) {
            mCachedWeather.put(cityWeather.getCity().getId(), cityWeather);
        }
    }

    private void checkWeatherServer(List<CityWeather> weatherList, final LoadWeatherCallback callback) {
        for (final CityWeather weather : weatherList) {
            mRemoteDataSource.loadWeatherData(weather.getCity().getOpenweatherId(),
                    new RemoteDataSource.GetWeatherCallback() {
                        @Override
                        public void onDataLoaded(String cityId, Weather weather) {
                            mLocalDataSource.updateWeather(cityId, weather);
                            callback.onDataLoaded(cityId, weather);
                        }

                        @Override
                        public void onDataNotAvailable(String cityId) {
                            callback.onDataNotAvailable(cityId);
                        }
                    });
        }
    }

    @Override
    public void findCitiesByPartOfTheName(String partOfTheName, int count,
                                          final FindCityListCallback callback) {
        mLocalDataSource.findCitiesByPartOfTheName(partOfTheName, count,
                new LocalDataSource.FindCityListCallback() {
                    @Override
                    public void onDataFound(List<City> cityList) {
                        callback.onDataFound(cityList);
                    }
                });
    }

    @Override
    public void addNewCityToChosenCityList(City city) throws SQLiteException {
        mLocalDataSource.addNewCityToChosenCityList(city);
    }
}
