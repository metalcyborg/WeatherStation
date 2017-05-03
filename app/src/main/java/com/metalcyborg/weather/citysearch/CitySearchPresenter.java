package com.metalcyborg.weather.citysearch;

import android.support.annotation.NonNull;

import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.source.WeatherDataSource;
import com.metalcyborg.weather.data.source.WeatherRepository;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by metalcyborg on 16.04.17.
 */

public class CitySearchPresenter implements CitySearchContract.Presenter {

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
        if (!mRepository.isCitiesDataAdded()) {
            addCitiesDataToRepository();
        } else {
            // Check ParseCitiesService running

        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void findCitiesByPartOfTheName(String partOfTheName) {
        mRepository.findCitiesByPartOfTheName(partOfTheName,
                new WeatherDataSource.FindCityListCallback() {
                    @Override
                    public void onDataFound(List<City> cityList) {
                        mView.showCityList(cityList);
                    }
                });
    }

    @Override
    public void addCityToWeatherList(City city) {
        mRepository.addNewCityToWeatherList(city);
        mView.showWeatherList();
    }

    private void addCitiesDataToRepository() {
        mView.setProgressVisibility(true);
        mView.setSearchActionVisibility(false);
        mRepository.addCitiesData(new WeatherDataSource.LoadCityDataCallback() {
            @Override
            public void onDataLoaded() {
                mView.setProgressVisibility(false);
                mView.setSearchActionVisibility(true);
                mView.showTypeCityNameMessage();
            }

            @Override
            public void onError() {
                mView.showErrorMessage();
                mView.setProgressVisibility(false);
            }
        });
    }
}
