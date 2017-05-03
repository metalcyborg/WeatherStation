package com.metalcyborg.weather.citylist;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.metalcyborg.weather.data.source.WeatherDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by metalcyborg on 16.04.17.
 */

public class CityListPresenter implements CityListContract.Presenter {

    private WeatherDataSource mRepository;
    private CityListContract.View mView;

    public CityListPresenter(@NonNull WeatherDataSource repository,
                             @NonNull CityListContract.View view) {
        mRepository = checkNotNull(repository);
        mView = checkNotNull(view);

        mView.setPresenter(this);
    }

    @Override
    public void start() {
        if(mRepository.isCitiesDataAdded()) {

        } else {
            // Check service running

        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void addNewCity() {
        mView.showCitySearch();
    }

    @Override
    public void result(int requestCode, int resultCode, Bundle extras) {

    }

    @Override
    public void onParseServiceBinded() {

    }
}
