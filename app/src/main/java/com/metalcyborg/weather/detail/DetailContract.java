package com.metalcyborg.weather.detail;

import com.metalcyborg.weather.BasePresenter;
import com.metalcyborg.weather.BaseView;

/**
 * Created by root on 13.04.17.
 */

public interface DetailContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

    }
}
