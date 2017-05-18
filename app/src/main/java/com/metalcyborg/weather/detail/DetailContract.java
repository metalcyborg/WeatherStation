package com.metalcyborg.weather.detail;

import com.metalcyborg.weather.BasePresenter;
import com.metalcyborg.weather.BaseView;
import com.metalcyborg.weather.data.Weather;

import java.util.List;

/**
 * Created by root on 13.04.17.
 */

public interface DetailContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void show3HForecast(List<Weather> forecast);

        void show13DForecast(List<Weather> forecast);

        void show3hForecastError();

        void show13DForecastError();

        void setLoadingIndicator(boolean indicator);
    }

    interface Presenter extends BasePresenter {

        void setParameters(String cityId);
    }
}
