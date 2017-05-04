package com.metalcyborg.weather.citylist;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.metalcyborg.weather.data.Weather;
import com.metalcyborg.weather.data.source.WeatherDataSource;

import java.util.List;

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
        mView.setProgressVisibility(true);
        if(mRepository.isCitiesDataAdded()) {
            loadWeatherData();
        } else {
            mView.bindParseService();
        }
    }

    @Override
    public void stop() {
        mView.stopServiceInteractions();
    }

    @Override
    public void addNewCity() {
        mView.showCitySearch();
    }

    @Override
    public void result(int requestCode, int resultCode, Bundle extras) {

    }

    @Override
    public void onParseServiceBound() {
        mView.setProgressVisibility(true);

        CityListContract.ParseCompleteListener parseCompleteListener =
                new CityListContract.ParseCompleteListener() {
            @Override
            public void onParseComplete() {
                mView.stopServiceInteractions();
                mRepository.setCitiesDataAdded();
                mView.setParseCitiesDataMessageVisibility(false);
                loadWeatherData();
            }

            @Override
            public void onParseError() {
                mView.stopServiceInteractions();
                mView.setParseCitiesDataMessageVisibility(false);
                mView.setParseErrorMessageVisibility(true);
            }
        };

        mView.registerParseCompleteListener(parseCompleteListener);
        mView.setParseCitiesDataMessageVisibility(true);

        if(!mView.isServiceRunning()) {
            // Run service
            mView.parseCitiesData();
        }
    }

    private void loadWeatherData() {
        mRepository.loadWeatherData(new WeatherDataSource.LoadWeatherListCallback() {
            @Override
            public void onDataLoaded(List<Weather> weatherData) {
                mView.showWeatherList(weatherData);
                mView.setProgressVisibility(false);
                mView.setFabVisibility(true);
            }

            @Override
            public void onError() {
                mView.setProgressVisibility(false);
                mView.setWeatherLoadingErrorMessageVisibility(true);
            }
        });
    }
}
