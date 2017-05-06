package com.metalcyborg.weather.data.source;

import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;

import com.metalcyborg.weather.citylist.parseservice.CityData;
import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.source.remote.RemoteDataSource;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class WeatherRepository implements WeatherDataSource {

    private static volatile WeatherRepository mInstance;
    private WeatherDataSource mLocalDataSource;
    private RemoteDataSource mRemoteDataSource;

    private WeatherRepository(@NonNull WeatherDataSource localDataSource,
                              @NonNull RemoteDataSource remoteDataSource) {
        mLocalDataSource = checkNotNull(localDataSource);
        mRemoteDataSource = checkNotNull(remoteDataSource);
    }

    public static WeatherRepository getInstance(WeatherDataSource localDataSource,
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
    public void loadWeatherData(LoadWeatherListCallback callback) {

    }

    @Override
    public void findCitiesByPartOfTheName(String partOfTheName, int count,
                                          final FindCityListCallback callback) {
        mLocalDataSource.findCitiesByPartOfTheName(partOfTheName, count,
                new FindCityListCallback() {
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
