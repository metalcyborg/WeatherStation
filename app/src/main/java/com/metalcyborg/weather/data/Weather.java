package com.metalcyborg.weather.data;

import android.support.annotation.NonNull;

public final class Weather {

    private float mTemperature;

    public Weather() {

    }

    public float getTemperature() {
        return mTemperature;
    }

    public void setTemperature(float temperature) {
        mTemperature = temperature;
    }
}
