package com.metalcyborg.weather.citysearch;

import android.support.annotation.NonNull;

import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.source.WeatherDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by metalcyborg on 16.04.17.
 */

public class CitySearchPresenter implements CitySearchContract.Presenter {

    private WeatherDataSource mRepository;
    private CitySearchContract.View mView;

    public CitySearchPresenter(@NonNull WeatherDataSource repository,
                               @NonNull CitySearchContract.View view) {
        mRepository = checkNotNull(repository);
        mView = checkNotNull(view);
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void findCitiesByPartOfTheName(String partOfTheName) {

    }

    @Override
    public void addCityToWeatherList(City city) {

    }
}
