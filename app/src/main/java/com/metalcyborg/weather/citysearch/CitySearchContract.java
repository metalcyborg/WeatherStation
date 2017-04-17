package com.metalcyborg.weather.citysearch;

import com.metalcyborg.weather.BasePresenter;
import com.metalcyborg.weather.BaseView;
import com.metalcyborg.weather.data.City;

import java.util.List;

/**
 * Created by root on 14.04.17.
 */

public interface CitySearchContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void setProgressVisibility(boolean visibility);

        void setSearchActionVisibility(boolean visibility);

        void showErrorMessage();

        void showTypeCityNameMessage();

        void showCityList(List<City> cityList);

        void showWeatherList();
    }

    interface Presenter extends BasePresenter {

        void findCitiesByPartOfTheName(String partOfTheName);

        void addCityToWeatherList(City city);
    }
}
