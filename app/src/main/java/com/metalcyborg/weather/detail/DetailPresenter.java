package com.metalcyborg.weather.detail;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.test.espresso.IdlingResource;

import com.metalcyborg.weather.ConnectivityReceiver;
import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.Weather;
import com.metalcyborg.weather.data.WeatherDetails;
import com.metalcyborg.weather.data.source.WeatherDataSource;
import com.metalcyborg.weather.util.EspressoIdlingResource;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


public class DetailPresenter implements DetailContract.Presenter,
        ConnectivityReceiver.ConnectivityListener {

    private WeatherDataSource mRepository;
    private DetailContract.View mView;
    private City mCity;
    private WeatherDetails mDetails;
    private ConnectivityManager mConnectivityManager;
    private ConnectivityReceiver mConnectivityReceiver;
    private boolean mFirstConnection = true;

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

        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected()) {
            mView.showMissingInternetConnectionMessage();
        }

        mView.displayCurrentWeatherDetails(mCity.getName(), mDetails);
        loadForecastData();

        mConnectivityReceiver = new ConnectivityReceiver();
        mConnectivityReceiver.setConnectivityListener(this);
        mView.registerConnectivityReceiver(mConnectivityReceiver);
    }

    @Override
    public void stop() {
        mView.unregisterConnectivityReceiver(mConnectivityReceiver);
    }

    @Override
    public void loadForecastData() {
        mView.setLoadingIndicator(true);

        mRepository.load3HForecastData(mCity.getOpenWeatherId(), new WeatherDataSource.LoadForecastCallback() {
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

        mRepository.load13DForecastData(mCity.getOpenWeatherId(), new WeatherDataSource.LoadForecastCallback() {
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
    public void setParameters(City city, WeatherDetails details) {
        mCity = city;
        mDetails = details;
    }

    @Override
    public void onConnectionChanged(boolean connected) {
        if(connected) {
            if(!mFirstConnection) {
                loadForecastData();
            } else {
                mFirstConnection = false;
            }
        } else {
            mView.showMissingInternetConnectionMessage();
            mFirstConnection = false;
        }
    }
}
