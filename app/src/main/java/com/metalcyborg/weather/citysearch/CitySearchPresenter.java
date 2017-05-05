package com.metalcyborg.weather.citysearch;

import android.support.annotation.NonNull;
import android.util.Log;

import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.source.WeatherDataSource;
import com.metalcyborg.weather.data.source.WeatherRepository;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by metalcyborg on 16.04.17.
 */

public class CitySearchPresenter implements CitySearchContract.Presenter {

    private static final int QUERY_RESULT_LIMIT = 20;
    private static final String TAG = "CitySearchPresenter";
    private WeatherRepository mRepository;
    private CitySearchContract.View mView;

    public CitySearchPresenter(@NonNull WeatherRepository repository,
                               @NonNull CitySearchContract.View view) {
        mRepository = checkNotNull(repository);
        mView = checkNotNull(view);
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void findCitiesByPartOfTheName(String partOfTheName) {
        mRepository.findCitiesByPartOfTheName(partOfTheName, QUERY_RESULT_LIMIT,
                new WeatherDataSource.FindCityListCallback() {
                    @Override
                    public void onDataFound(List<City> cityList) {
                        mView.showCityList(cityList);
                    }
                });
    }

    @Override
    public void addCityToWeatherList(City city) {
        Log.d(TAG, "addCityToWeatherList: " + city.getName());
        mRepository.addNewCityToWeatherList(city);
        mView.showWeatherList();
    }

    private void addCitiesDataToRepository() {
        mView.setProgressVisibility(true);
        mView.setSearchActionVisibility(false);
    }
}
