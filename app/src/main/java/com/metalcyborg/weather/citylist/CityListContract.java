package com.metalcyborg.weather.citylist;

import android.os.Bundle;

import com.metalcyborg.weather.BasePresenter;
import com.metalcyborg.weather.BaseView;
import com.metalcyborg.weather.data.CityWeather;
import com.metalcyborg.weather.data.Weather;

import java.util.List;

/**
 * Created by root on 13.04.17.
 */

public interface CityListContract {

    interface ParseCompleteListener {

        void onParseComplete();

        void onParseError();
    }

    interface View extends BaseView<Presenter> {
        void showCitySearch();

        boolean isActive();

        void setProgressVisibility(boolean visibility);

        void setParseCitiesDataMessageVisibility(boolean visibility);

        void setParseErrorMessageVisibility(boolean visibility);

        void setWeatherLoadingErrorMessageVisibility(boolean visibility);

        void setFabVisibility(boolean visibility);

        void showWeatherList(List<CityWeather> weatherList);

        void bindParseService();

        boolean isServiceRunning();

        void parseCitiesData();

        void registerParseCompleteListener(ParseCompleteListener listener);

        void stopServiceInteractions();

        void updateItem(String cityId, Weather weather);
    }

    interface Presenter extends BasePresenter {
        void addNewCity();

        void onParseServiceBound();
    }
}