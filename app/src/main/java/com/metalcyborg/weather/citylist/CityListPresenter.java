package com.metalcyborg.weather.citylist;

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

    }
}
