package com.metalcyborg.weather.data.source.remote;

import com.google.gson.annotations.SerializedName;

/**
 * Created by metalcyborg on 14.05.17.
 */

public class CurrentWeatherModel {

    @SerializedName("main")
    private Main mMain;

    public Main getMain() {
        return mMain;
    }

    public void setMain(Main main) {
        mMain = main;
    }

    public class Main {

        @SerializedName("temp")
        private float mTemp;

        public float getTemp() {
            return mTemp;
        }

        public void setTemp(float temp) {
            mTemp = temp;
        }
    }
}
