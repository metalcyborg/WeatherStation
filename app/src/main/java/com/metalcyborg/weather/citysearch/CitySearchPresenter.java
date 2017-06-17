package com.metalcyborg.weather.citysearch;

import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.source.WeatherDataSource;
import com.metalcyborg.weather.data.source.WeatherRepository;
import com.metalcyborg.weather.util.FragmentScoped;

import java.util.List;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class CitySearchPresenter implements CitySearchContract.Presenter {

    private static final int QUERY_RESULT_LIMIT = 20;
    private WeatherRepository mRepository;
    private CitySearchContract.View mView;

    @Inject
    public CitySearchPresenter(WeatherRepository repository,
                               CitySearchContract.View view) {
        mRepository = checkNotNull(repository);
        mView = checkNotNull(view);
    }

    @Inject
    public void setupListeners() {
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
        mRepository.addNewCityToChosenCityList(city);
        mView.showWeatherList();
    }

    private void addCitiesDataToRepository() {
        mView.setProgressVisibility(true);
        mView.setSearchActionVisibility(false);
    }
}
