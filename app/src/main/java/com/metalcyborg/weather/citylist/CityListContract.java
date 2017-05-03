package com.metalcyborg.weather.citylist;

import android.os.Bundle;

import com.metalcyborg.weather.BasePresenter;
import com.metalcyborg.weather.BaseView;
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

        void setFabVisibility(boolean visibility);

        void showWeatherList(List<Weather> weatherList);

        void bindParseService();

        void unbindParseService();

        boolean isServiceRunning();

        void parseCitiesData();

        void registerParseCompleteListener(ParseCompleteListener listener);

        void unregisterParseCompleteListener(ParseCompleteListener listener);
    }

    interface Presenter extends BasePresenter {
        void addNewCity();

        void result(int requestCode, int resultCode, Bundle extras);

        void onParseServiceBinded();
    }
}