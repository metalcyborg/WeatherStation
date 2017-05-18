package com.metalcyborg.weather.detail;

import android.support.annotation.NonNull;

import com.metalcyborg.weather.data.source.WeatherDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by metalcyborg on 16.04.17.
 */

public class DetailPresenter implements DetailContract.Presenter {

    private WeatherDataSource mRepository;
    private DetailContract.View mView;

    public DetailPresenter(@NonNull WeatherDataSource repository,
                           @NonNull DetailContract.View view) {
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

    private void loadForecastData() {

    }

    @Override
    public void setParameters(String cityId) {

    }
}
