package com.metalcyborg.weather.citylist;

import com.metalcyborg.weather.BasePresenter;
import com.metalcyborg.weather.BaseView;
import com.metalcyborg.weather.data.CityWeather;
import com.metalcyborg.weather.data.Weather;

import java.util.List;

public interface CityListContract {

    interface ParseCompleteListener {

        void onParseComplete();

        void onParseError();
    }

    interface View extends BaseView<Presenter> {
        void showCitySearch();

        boolean isActive();

        void setProgressVisibility(boolean visibility);

        void setFabVisibility(boolean visibility);

        void showWeatherList(List<CityWeather> weatherList);

        void updateItem(String cityId, Weather weather);

        void showForecast(CityWeather cityWeather);

        void showSettings();

        void deleteSelectedItems();

        void showWeatherLoadingErrorMessage();

        void showCopyDatabaseError();
    }

    interface Presenter extends BasePresenter {
        void addNewCity();

        void onWeatherItemClicked(CityWeather cityWeather);

        void onWeatherItemLongClicked(CityWeather cityWeather);

        void onSettingsMenuItemClick();

        void deleteItems(List<CityWeather> items);
    }
}