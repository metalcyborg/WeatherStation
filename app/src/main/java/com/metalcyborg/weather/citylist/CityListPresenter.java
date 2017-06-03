package com.metalcyborg.weather.citylist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.CityWeather;
import com.metalcyborg.weather.data.Weather;
import com.metalcyborg.weather.data.source.WeatherDataSource;
import com.metalcyborg.weather.util.EspressoIdlingResource;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


public class CityListPresenter implements CityListContract.Presenter,
        LoaderManager.LoaderCallbacks<Boolean> {

    private WeatherDataSource mRepository;
    private CityListContract.View mView;
    private DbLoader mDbLoader;
    private LoaderManager mLoaderManager;

    public CityListPresenter(@NonNull WeatherDataSource repository,
                             @NonNull CityListContract.View view,
                             @NonNull DbLoader dbLoader,
                             @NonNull LoaderManager loaderManager) {
        mRepository = checkNotNull(repository);
        mView = checkNotNull(view);
        mDbLoader = checkNotNull(dbLoader);
        mLoaderManager = checkNotNull(loaderManager);

        mView.setPresenter(this);
    }

    @Override
    public void start() {
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
    }

    @Override
    public void stop() {

    }

    @Override
    public void loadCityList() {
        EspressoIdlingResource.increment();
        mRepository.loadWeatherData(new WeatherDataSource.LoadWeatherCallback() {
            @Override
            public void onDataListLoaded(List<CityWeather> weatherData) {
                EspressoIdlingResource.decrement();

                mView.setProgressVisibility(false);
                if(weatherData.isEmpty()) {
                    mView.showAddCityMessage();
                } else {
                    mView.showWeatherList(weatherData);
                }
            }

            @Override
            public void onDataListNotAvailable() {
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
        EspressoIdlingResource.decrement();
    }

    @Override
    public void onLoaderReset(Loader<Boolean> loader) {
        mView.showCopyDatabaseError();
    }
}
