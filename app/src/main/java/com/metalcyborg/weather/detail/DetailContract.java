package com.metalcyborg.weather.detail;

import com.metalcyborg.weather.BasePresenter;
import com.metalcyborg.weather.BaseView;
import com.metalcyborg.weather.data.Weather;
import com.metalcyborg.weather.data.WeatherDetails;

import java.util.List;

public interface DetailContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void show3HForecast(List<Weather> forecast);

        void show13DForecast(List<Weather> forecast);

        void show3hForecastError();

        void show13DForecastError();

        void showMissingInternetConnectionMessage();

        void setLoadingIndicator(boolean indicator);

        void displayCurrentWeatherDetails(String cityName, WeatherDetails details);
    }

    interface Presenter extends BasePresenter {

        void setParameters(String cityId, String cityName, WeatherDetails details);
    }
}
