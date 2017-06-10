package com.metalcyborg.weather.data.source.remote.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecast3Hours {

    @SerializedName("list")
    private List<Weather> mWeatherList;

    public List<Weather> getWeatherList() {
        return mWeatherList;
    }

    public static class Weather {

        @SerializedName("dt")
        private long mDateTime;

        @SerializedName("main")
        private Main mMain;

        @SerializedName("weather")
        private List<Description> mWeatherDescription;

        @SerializedName("clouds")
        private Clouds mClouds;

        @SerializedName("wind")
        private Wind mWind;

        @SerializedName("rain")
        private Rain mRain;

        @SerializedName("snow")
        private Snow mSnow;

        @SerializedName("sys")
        private Sys mSys;

        @SerializedName("dt_txt")
        private String mDateTimeText;

        public long getDateTime() {
            return mDateTime;
        }

        public Main getMain() {
            return mMain;
        }

        public List<Description> getWeatherDescription() {
            return mWeatherDescription;
        }

        public Clouds getClouds() {
            return mClouds;
        }

        public Wind getWind() {
            return mWind;
        }

        public Rain getRain() {
            return mRain;
        }

        public Snow getSnow() {
            return mSnow;
        }

        public Sys getSys() {
            return mSys;
        }

        public String getDateTimeText() {
            return mDateTimeText;
        }
    }

    public static class Main {

        @SerializedName("temp")
        private float mTemp;

        @SerializedName("temp_min")
        private float mTempMin;

        @SerializedName("temp_max")
        private float mTempMax;

        @SerializedName("pressure")
        private float mPressure;

        @SerializedName("sea_level")
        private float mSeaLevel;

        @SerializedName("grnd_level")
        private float mGroundLevel;

        @SerializedName("humidity")
        private float mHumidity;

        @SerializedName("temp_kf")
        private float mTempKf;

        public float getTemp() {
            return mTemp;
        }

        public float getTempMin() {
            return mTempMin;
        }

        public float getTempMax() {
            return mTempMax;
        }

        public float getPressure() {
            return mPressure;
        }

        public float getSeaLevel() {
            return mSeaLevel;
        }

        public float getGroundLevel() {
            return mGroundLevel;
        }

        public float getHumidity() {
            return mHumidity;
        }

        public float getTempKf() {
            return mTempKf;
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

    public static class Clouds {

        @SerializedName("all")
        private int mCloudiness;

        public int getCloudiness() {
            return mCloudiness;
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

    public static class Sys {

        @SuppressWarnings("pod")
        private String mPod;

        public String getPod() {
            return mPod;
        }
    }
}
