package com.metalcyborg.weather.citylist;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.metalcyborg.weather.ConnectivityReceiver;
import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.CityWeather;
import com.metalcyborg.weather.data.Weather;
import com.metalcyborg.weather.data.source.WeatherDataSource;
import com.metalcyborg.weather.util.EspressoIdlingResource;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


public class CityListPresenter implements CityListContract.Presenter,
        LoaderManager.LoaderCallbacks<Boolean>,ConnectivityReceiver.ConnectivityListener {

    private static final String TAG = "CityListPresenter";
    private WeatherDataSource mRepository;
    private CityListContract.View mView;
    private DbLoader mDbLoader;
    private LoaderManager mLoaderManager;
    private ConnectivityManager mConnectivityManager;
    private ConnectivityReceiver mConnectivityReceiver;
    private boolean mFirstConnection = true;

    public CityListPresenter(@NonNull WeatherDataSource repository,
                             @NonNull CityListContract.View view,
                             @NonNull DbLoader dbLoader,
                             @NonNull LoaderManager loaderManager,
                             @NonNull ConnectivityManager connectivityManager) {
        mRepository = checkNotNull(repository);
        mView = checkNotNull(view);
        mDbLoader = checkNotNull(dbLoader);
        mLoaderManager = checkNotNull(loaderManager);
        mConnectivityManager = checkNotNull(connectivityManager);

        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mFirstConnection = true;
        EspressoIdlingResource.increment();

        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected()) {
            mView.showMissingInternetConnectionMessage();
        }

        mView.setProgressVisibility(true);
        if(mRepository.isCitiesDataAdded()) {
            mView.setFabVisibility(true);
            loadCityList();
        } else {
            if(mLoaderManager.getLoader(0) != null && mLoaderManager.getLoader(0).isStarted()) {
                mLoaderManager.initLoader(0, null, this);
            } else {
                mLoaderManager.initLoader(0, null, this).forceLoad();
            }
        }

        mConnectivityReceiver = new ConnectivityReceiver();
        mConnectivityReceiver.setConnectivityListener(this);
        mView.registerConnectivityReceiver(mConnectivityReceiver);
    }

    @Override
    public void stop() {
        mView.unregisterConnectivityReceiver(mConnectivityReceiver);
        mConnectivityReceiver = null;
    }

    @Override
    public void loadCityList() {
        mRepository.loadWeatherData(new WeatherDataSource.LoadWeatherCallback() {
            @Override
            public void onDataListLoaded(List<CityWeather> weatherData) {
                if(!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement();
                }

                mView.setProgressVisibility(false);
                if(weatherData.isEmpty()) {
                    mView.showAddCityMessage();
                } else {
                    mView.showWeatherList(weatherData);
                }
            }

            @Override
            public void onDataListNotAvailable() {
                if(!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement();
                }
                mView.setProgressVisibility(false);
                mView.showWeatherLoadingErrorMessage();
            }

            @Override
            public void onDataLoaded(String cityId, Weather weather) {
                mView.updateItem(cityId, weather);
            }

            @Override
            public void onDataNotAvailable(String cityId) {
                mView.updateItem(cityId, null);
            }
        });
    }

    @Override
    public void addNewCity() {
        mView.showCitySearch();
    }

    @Override
    public void onWeatherItemClicked(CityWeather cityWeather) {
        if(cityWeather.getWeather() != null) {
            mView.showForecast(cityWeather);
        }
    }

    @Override
    public void onWeatherItemLongClicked(CityWeather cityWeather) {

    }

    @Override
    public void onSettingsMenuItemClick() {
        mView.showSettings();
    }

    @Override
    public void deleteItems(List<CityWeather> items) {
        mView.deleteSelectedItems();
        if(mView.getCityCount() == 0) {
            mView.showAddCityMessage();
        }
        List<City> cityList = new ArrayList<>();
        for(CityWeather cityWeather : items) {
            cityList.add(cityWeather.getCity());
        }
        mRepository.deleteCitiesFromChosenCityList(cityList);
    }

    @Override
    public Loader<Boolean> onCreateLoader(int id, Bundle args) {
        EspressoIdlingResource.increment();
        return mDbLoader;
    }

    @Override
    public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
        if(data) {
            mView.setFabVisibility(true);
            loadCityList();
        } else {
            mView.showCopyDatabaseError();
        }

        if(!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
            EspressoIdlingResource.decrement();
        }
    }

    @Override
    public void onLoaderReset(Loader<Boolean> loader) {
        mView.showCopyDatabaseError();
    }

    @Override
    public void onConnectionChanged(boolean connected) {
        if(connected) {
            if(!mFirstConnection && mRepository.isCitiesDataAdded()) {
                loadCityList();
            } else {
                mFirstConnection = false;
            }
        } else {
            mView.showMissingInternetConnectionMessage();
            mFirstConnection = false;
        }
    }
}
