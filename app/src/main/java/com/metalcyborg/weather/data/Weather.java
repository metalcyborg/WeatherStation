package com.metalcyborg.weather.data;

import android.support.annotation.NonNull;

public final class Weather {

    private String mId;
    private float mTemperature;

    public Weather(String id) {
        mId = id;
    }

    public String getId() {
        return mId;
    }

    public float getTemperature() {
        return mTemperature;
    }
}
