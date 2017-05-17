package com.metalcyborg.weather.data.source.remote.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CurrentWeather {

    @SerializedName("dt")
    private long mDateTime;

    @SerializedName("weather")
    private List<WeatherDescription> mWeatherDescription;

    @SerializedName("main")
    private Main mMain;

    @SerializedName("wind")
    private Wind mWind;

    @SerializedName("clouds")
    private Clouds mClouds;

    @SerializedName("sys")
    private Sys mSys;

    @SerializedName("rain")
    private Rain mRain;

    @SerializedName("snow")
    private Snow mSnow;

    public long getDateTime() {
        return mDateTime;
    }

    public Main getMain() {
        return mMain;
    }

    public List<WeatherDescription> getWeatherDescription() {
        return mWeatherDescription;
    }

    public Wind getWind() {
        return mWind;
    }

    public Clouds getClouds() {
        return mClouds;
    }

    public Sys getSys() {
        return mSys;
    }

    public Rain getRain() {
        return mRain;
    }

    public Snow getSnow() {
        return mSnow;
    }

    public static class Main {

        @SerializedName("temp")
        private float mTemp;

        @SerializedName("pressure")
        private float mPressure;

        @SerializedName("humidity")
        private float mHumidity;

        public float getTemp() {
            return mTemp;
        }

        public float getPressure() {
            return mPressure;
        }

        public float getHumidity() {
            return mHumidity;
        }
    }

    public static class WeatherDescription {
        @SerializedName("id")
        private int mId;

        @SerializedName("main")
        private String mMain;

        @SerializedName("description")
        private String mDetail;

        @SerializedName("icon")
        private String mIcon;

        public int getId() {
            return mId;
        }

        public String getMain() {
            return mMain;
        }

        public String getDetail() {
            return mDetail;
        }

        public String getIcon() {
            return mIcon;
        }
    }

    public static class Wind {

        @SerializedName("speed")
        private float mSpeed;

        @SerializedName("deg")
        private float mDeg;

        public float getSpeed() {
            return mSpeed;
        }

        public float getDeg() {
            return mDeg;
        }
    }

    public static class Clouds {

        @SerializedName("all")
        private int mCloudiness;

        public int getCloudiness() {
            return mCloudiness;
        }
    }

    public static class Sys {

        @SerializedName("sunrise")
        private long mSunrise;

        @SerializedName("sunset")
        private long mSunset;

        public long getSunrise() {
            return mSunrise;
        }

        public long getSunset() {
            return mSunset;
        }
    }

    public static class Rain {

        @SerializedName("3h")
        private float mVolume3H;

        public float getVolume3H() {
            return mVolume3H;
        }
    }

    public static class Snow {

        @SerializedName("3h")
        private float mVolume3H;

        public float getVolume3H() {
            return mVolume3H;
        }
    }
}

