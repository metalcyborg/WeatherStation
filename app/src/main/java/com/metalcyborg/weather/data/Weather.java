package com.metalcyborg.weather.data;

import android.support.annotation.NonNull;

/**
 * Created by root on 13.04.17.
 */

public final class Weather {

    private City mCity;
    private float mTemperature;

    public City getCity() {
        return mCity;
    }

    public float getTemperature() {
        return mTemperature;
    }
}
