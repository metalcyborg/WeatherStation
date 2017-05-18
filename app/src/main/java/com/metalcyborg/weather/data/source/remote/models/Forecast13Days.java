package com.metalcyborg.weather.data.source.remote.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by metalcyborg on 16.05.17.
 */

public class Forecast13Days {

    @SerializedName("list")
    private List<Weather> mWeatherList;

    public List<Weather> getWeatherList() {
        return mWeatherList;
    }

    public static class Weather {

        @SerializedName("dt")
        private long mDateTime;

        @SerializedName("temp")
        private Temperature mTemp;

        @SerializedName("pressure")
        private float mPressure;

        @SerializedName("humidity")
        private float mHumidity;

        @SerializedName("weather")
        private List<Description> mWeatherDescription;

        @SerializedName("speed")
        private float mWindSpeed;

        @SerializedName("deg")
        private float mWindDeg;

        @SerializedName("clouds")
        private int mClouds;

        @SerializedName("rain")
        private float mRain;

        @SerializedName("snow")
        private float mSnow;

        public long getDateTime() {
            return mDateTime;
        }

        public Temperature getTemp() {
            return mTemp;
        }

        public float getPressure() {
            return mPressure;
        }

        public float getHumidity() {
            return mHumidity;
        }

        public List<Description> getWeatherDescription() {
            return mWeatherDescription;
        }

        public float getWindSpeed() {
            return mWindSpeed;
        }

        public float getWindDeg() {
            return mWindDeg;
        }

        public int getClouds() {
            return mClouds;
        }

        public float getRain() {
            return mRain;
        }

        public float getSnow() {
            return mSnow;
        }
    }

    public static class Temperature {

        @SerializedName("day")
        private float mDay;

        @SerializedName("min")
        private float mMin;

        @SerializedName("max")
        private float mMax;

        @SerializedName("night")
        private float mNight;

        @SerializedName("eve")
        private float mEvening;

        @SerializedName("morn")
        private float mMorning;

        public float getDay() {
            return mDay;
        }

        public float getMin() {
            return mMin;
        }

        public float getMax() {
            return mMax;
        }

        public float getNight() {
            return mNight;
        }

        public float getEvening() {
            return mEvening;
        }

        public float getMorning() {
            return mMorning;
        }
    }

    public static class Description {
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
}
