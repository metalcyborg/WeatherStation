package com.metalcyborg.weather.detail;

import com.metalcyborg.weather.BasePresenter;
import com.metalcyborg.weather.BaseView;
import com.metalcyborg.weather.ConnectivityReceiver;
import com.metalcyborg.weather.data.City;
import com.metalcyborg.weather.data.Weather;
import com.metalcyborg.weather.data.WeatherDetails;

import java.util.List;

public interface DetailContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void show3HForecast(List<Weather> forecast, String timeZone);

        void show13DForecast(List<Weather> forecast, String timeZone);

        void show3hForecastError();

        void show13DForecastError();

        void showMissingInternetConnectionMessage();

        void setLoadingIndicator(boolean indicator);

        void displayCurrentWeatherDetails(String cityName, WeatherDetails details, String timeZone);

        void registerConnectivityReceiver(ConnectivityReceiver receiver);

        void unregisterConnectivityReceiver(ConnectivityReceiver receiver);
    }

    interface Presenter extends BasePresenter {

        void setParameters(City city, WeatherDetails details);

        void loadForecastData();
    }
}
