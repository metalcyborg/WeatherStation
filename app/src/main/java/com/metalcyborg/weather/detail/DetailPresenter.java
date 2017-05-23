package com.metalcyborg.weather.detail;

import android.support.annotation.NonNull;

import com.metalcyborg.weather.data.Weather;
import com.metalcyborg.weather.data.source.WeatherDataSource;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by metalcyborg on 16.04.17.
 */

public class DetailPresenter implements DetailContract.Presenter {

    private WeatherDataSource mRepository;
    private DetailContract.View mView;
    private String mCityId;
    private String mCityName;
    private float mTemperature;
    private String mIcon;

    public DetailPresenter(@NonNull WeatherDataSource repository,
                           @NonNull DetailContract.View view) {
        mRepository = checkNotNull(repository);
        mView = checkNotNull(view);
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.displayHeader(mCityName, mTemperature, mIcon);
        loadForecastData();
    }

    @Override
    public void stop() {

    }

    private void loadForecastData() {
        mView.setLoadingIndicator(true);
        mRepository.load3HForecastData(mCityId, new WeatherDataSource.LoadForecastCallback() {
            @Override
            public void onDataLoaded(List<Weather> forecast) {
                mView.setLoadingIndicator(false);
                mView.show3HForecast(forecast);
            }

            @Override
            public void onDataNotAvailable() {
                mView.setLoadingIndicator(false);
                mView.show3hForecastError();
            }
        });

        mRepository.load13DForecastData(mCityId, new WeatherDataSource.LoadForecastCallback() {
            @Override
            public void onDataLoaded(List<Weather> forecast) {
                mView.show13DForecast(forecast);
            }

            @Override
            public void onDataNotAvailable() {
                mView.show13DForecastError();
            }
        });
    }

    @Override
    public void setParameters(String cityId, String cityName, float temperature, String icon) {
        mCityId = cityId;
        mCityName = cityName;
        mTemperature = temperature;
        mIcon = icon;
    }
}
