package com.metalcyborg.weather.detail;

import android.net.ConnectivityManager;

import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.WeatherDetails;
import com.metalcyborg.weather.util.FragmentScoped;

import dagger.Module;
import dagger.Provides;

@Module
public class DetailPresenterModule {

    private DetailContract.View mView;
    private ConnectivityManager mConnectivityManager;
    private City mCity;
    private WeatherDetails mWeatherDetails;

    public DetailPresenterModule(DetailContract.View view,
                                 ConnectivityManager connectivityManager,
                                 City city,
                                 WeatherDetails details) {
        mView = view;
        mConnectivityManager = connectivityManager;
        mCity = city;
        mWeatherDetails = details;
    }

    @FragmentScoped
    @Provides
    public DetailContract.View provideDetailContractView() {
        return mView;
    }

    @FragmentScoped
    @Provides
    public ConnectivityManager provideConnectivityManager() {
        return mConnectivityManager;
    }

    @FragmentScoped
    @Provides
    public City provideCityData() {
        return mCity;
    }

    @FragmentScoped
    @Provides
    public WeatherDetails provideWeatherDetails() {
        return mWeatherDetails;
    }
}
