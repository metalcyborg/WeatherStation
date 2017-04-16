package com.metalcyborg.weather.citylist;

import com.metalcyborg.weather.BasePresenter;
import com.metalcyborg.weather.BaseView;

/**
 * Created by root on 13.04.17.
 */

public interface CityListContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}