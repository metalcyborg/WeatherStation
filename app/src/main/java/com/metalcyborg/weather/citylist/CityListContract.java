package com.metalcyborg.weather.citylist;

import android.os.Bundle;

import com.metalcyborg.weather.BasePresenter;
import com.metalcyborg.weather.BaseView;

/**
 * Created by root on 13.04.17.
 */

public interface CityListContract {

    interface View extends BaseView<Presenter> {
        void showCitySearch();

        boolean isActive();

        void showProgress();

        void hideProgress();
    }

    interface Presenter extends BasePresenter {
        void addNewCity();

        void result(int requestCode, int resultCode, Bundle extras);
    }
}