package com.metalcyborg.weather.citylist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

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
    private CityListContract.ParseCompleteListener mParseCompleteListener;

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
        if(mParseCompleteListener != null) {
            mView.unregisterParseCompleteListener(mParseCompleteListener);
        }

        if(mView.isBindedWithParseService()) {
            mView.unbindParseService();
        }
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
        mView.setProgressVisibility(true);

        mParseCompleteListener = new CityListContract.ParseCompleteListener() {
            @Override
            public void onParseComplete() {
                mView.unregisterParseCompleteListener(mParseCompleteListener);
                mView.unbindParseService();
                mParseCompleteListener = null;
                mRepository.setCitiesDataAdded();
                mView.setParseCitiesDataMessageVisibility(false);
                loadWeatherData();
            }

            @Override
            public void onParseError() {
                mView.unregisterParseCompleteListener(mParseCompleteListener);
                mView.unbindParseService();
                mParseCompleteListener = null;
                mView.setParseCitiesDataMessageVisibility(false);
                mView.setParseErrorMessageVisibility(true);
            }
        };

        mView.registerParseCompleteListener(mParseCompleteListener);
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
