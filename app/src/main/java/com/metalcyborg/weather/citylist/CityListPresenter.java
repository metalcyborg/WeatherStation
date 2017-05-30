package com.metalcyborg.weather.citylist;

import android.support.annotation.NonNull;

import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.CityWeather;
import com.metalcyborg.weather.data.Weather;
import com.metalcyborg.weather.data.source.WeatherDataSource;
import com.metalcyborg.weather.util.EspressoIdlingResource;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


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
            mView.setFabVisibility(true);
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
    public void onParseServiceBound() {
        mView.setProgressVisibility(true);

        CityListContract.ParseCompleteListener parseCompleteListener =
                new CityListContract.ParseCompleteListener() {
            @Override
            public void onParseComplete() {
                mView.stopServiceInteractions();
                mRepository.setCitiesDataAdded();
                mView.setParseCitiesDataMessageVisibility(false);
                mView.setFabVisibility(true);
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
        List<City> cityList = new ArrayList<>();
        for(CityWeather cityWeather : items) {
            cityList.add(cityWeather.getCity());
        }
        mRepository.deleteCitiesFromChosenCityList(cityList);
    }

    private void loadWeatherData() {
        EspressoIdlingResource.increment();
        mRepository.loadWeatherData(new WeatherDataSource.LoadWeatherCallback() {
            @Override
            public void onDataListLoaded(List<CityWeather> weatherData) {
                EspressoIdlingResource.decrement();
                mView.showWeatherList(weatherData);
                mView.setProgressVisibility(false);
            }

            @Override
            public void onDataListNotAvailable() {
                mView.setProgressVisibility(false);
                mView.setWeatherLoadingErrorMessageVisibility(true);
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
}
