package com.metalcyborg.weather.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {

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

    public Weather(long dateTime) {
        mDateTime = dateTime;
    }

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

    public void setDateTime(long dateTime) {
        mDateTime = dateTime;
    }

    public void setWeatherDescription(List<WeatherDescription> weatherDescription) {
        mWeatherDescription = weatherDescription;
    }

    public void setMain(Main main) {
        mMain = main;
    }

    public void setWind(Wind wind) {
        mWind = wind;
    }

    public void setClouds(Clouds clouds) {
        mClouds = clouds;
    }

    public void setSys(Sys sys) {
        mSys = sys;
    }

    public void setRain(Rain rain) {
        mRain = rain;
    }

    public void setSnow(Snow snow) {
        mSnow = snow;
    }

    public static class Main {

        @SerializedName("temp")
        private float mTemp;

        @SerializedName("pressure")
        private float mPressure;

        @SerializedName("humidity")
        private float mHumidity;

        public Main(float temp, float pressure, float humidity) {
            mTemp = temp;
            mPressure = pressure;
            mHumidity = humidity;
        }

        public float getTemp() {
            return mTemp;
        }

        public float getPressure() {
            return mPressure;
        }

        public float getHumidity() {
            return mHumidity;
        }

        public void setTemp(float temp) {
            mTemp = temp;
        }

        public void setPressure(float pressure) {
            mPressure = pressure;
        }

        public void setHumidity(float humidity) {
            mHumidity = humidity;
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

        public WeatherDescription(int id, String main, String detail, String icon) {
            mId = id;
            mMain = main;
            mDetail = detail;
            mIcon = icon;
        }

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

        public void setId(int id) {
            mId = id;
        }

        public void setMain(String main) {
            mMain = main;
        }

        public void setDetail(String detail) {
            mDetail = detail;
        }

        public void setIcon(String icon) {
            mIcon = icon;
        }
    }

    public static class Wind {

        @SerializedName("speed")
        private float mSpeed;

        @SerializedName("deg")
        private float mDeg;

        public Wind(float speed, float deg) {
            mSpeed = speed;
            mDeg = deg;
        }

        public float getSpeed() {
            return mSpeed;
        }

        public float getDeg() {
            return mDeg;
        }

        public void setSpeed(float speed) {
            mSpeed = speed;
        }

        public void setDeg(float deg) {
            mDeg = deg;
        }
    }

    public static class Clouds {

        @SerializedName("all")
        private int mCloudiness;

        public Clouds(int cloudiness) {
            mCloudiness = cloudiness;
        }

        public int getCloudiness() {
            return mCloudiness;
        }

        public void setCloudiness(int cloudiness) {
            mCloudiness = cloudiness;
        }
    }

    public static class Sys {

        @SerializedName("sunrise")
        private long mSunrise;

        @SerializedName("sunset")
        private long mSunset;

        public Sys(long sunrise, long sunset) {
            mSunrise = sunrise;
            mSunset = sunset;
        }

        public long getSunrise() {
            return mSunrise;
        }

        public long getSunset() {
            return mSunset;
        }

        public void setSunrise(long sunrise) {
            mSunrise = sunrise;
        }

        public void setSunset(long sunset) {
            mSunset = sunset;
        }
    }

    public static class Rain {

        @SerializedName("3h")
        private float mVolume3H;

        public Rain(float volume3H) {
            mVolume3H = volume3H;
        }

        public float getVolume3H() {
            return mVolume3H;
        }

        public void setVolume3H(float volume3H) {
            mVolume3H = volume3H;
        }
    }

    public static class Snow {

        @SerializedName("3h")
        private float mVolume3H;

        public Snow(float volume3H) {
            mVolume3H = volume3H;
        }

        public float getVolume3H() {
            return mVolume3H;
        }

        public void setVolume3H(float volume3H) {
            mVolume3H = volume3H;
        }
    }
}
