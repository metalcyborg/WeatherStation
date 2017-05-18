package com.metalcyborg.weather.data;

public class Weather {

    private long mDateTime;
    private WeatherDescription mWeatherDescription;
    private Main mMain;
    private Wind mWind;
    private Clouds mClouds;
    private Sys mSys;
    private Rain mRain;
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

    public WeatherDescription getWeatherDescription() {
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

    public void setWeatherDescription(WeatherDescription weatherDescription) {
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

        private float mDayTemp;
        private float mMorningTemp;
        private float mEveningTemp;
        private float mNightTemp;
        private float mPressure;
        private float mHumidity;

        public Main(float dayTemp, float nightTemp, float pressure, float humidity) {
            mDayTemp = dayTemp;
            mNightTemp = nightTemp; // optional, 0 by default
            mPressure = pressure;
            mHumidity = humidity;
        }

        public float getDayTemp() {
            return mDayTemp;
        }

        public float getMorningTemp() {
            return mMorningTemp;
        }

        public float getEveningTemp() {
            return mEveningTemp;
        }

        public float getNightTemp() {
            return mNightTemp;
        }

        public float getPressure() {
            return mPressure;
        }

        public float getHumidity() {
            return mHumidity;
        }

        public void setDayTemp(float dayTemp) {
            mDayTemp = dayTemp;
        }

        public void setMorningTemp(float morningTemp) {
            mMorningTemp = morningTemp;
        }

        public void setEveningTemp(float eveningTemp) {
            mEveningTemp = eveningTemp;
        }

        public void setNightTemp(float nightTemp) {
            mNightTemp = nightTemp;
        }

        public void setPressure(float pressure) {
            mPressure = pressure;
        }

        public void setHumidity(float humidity) {
            mHumidity = humidity;
        }
    }

    public static class WeatherDescription {

        private int mId;
        private String mMain;
        private String mDetail;
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

        private float mSpeed;
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

        private long mSunrise;
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
