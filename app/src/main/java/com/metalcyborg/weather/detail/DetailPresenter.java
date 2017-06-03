package com.metalcyborg.weather.detail;

import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.test.espresso.IdlingResource;

import com.metalcyborg.weather.data.Weather;
import com.metalcyborg.weather.data.WeatherDetails;
import com.metalcyborg.weather.data.source.WeatherDataSource;
import com.metalcyborg.weather.util.EspressoIdlingResource;

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
    private WeatherDetails mDetails;
    private ConnectivityManager mConnectivityManager;

    public DetailPresenter(@NonNull WeatherDataSource repository,
                           @NonNull DetailContract.View view,
                           @NonNull ConnectivityManager connectivityManager) {
        mRepository = checkNotNull(repository);
        mView = checkNotNull(view);
        mConnectivityManager = checkNotNull(connectivityManager);
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        EspressoIdlingResource.increment();
        mView.displayCurrentWeatherDetails(mCityName, mDetails);
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
                if(!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement();
                }

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
    public void setParameters(String cityId, String cityName, WeatherDetails details) {
        mCityId = cityId;
        mCityName = cityName;
        mDetails = details;
    }
}
