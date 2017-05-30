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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Main main = (Main) o;

            if (Float.compare(main.mDayTemp, mDayTemp) != 0) return false;
            if (Float.compare(main.mMorningTemp, mMorningTemp) != 0) return false;
            if (Float.compare(main.mEveningTemp, mEveningTemp) != 0) return false;
            if (Float.compare(main.mNightTemp, mNightTemp) != 0) return false;
            if (Float.compare(main.mPressure, mPressure) != 0) return false;
            return Float.compare(main.mHumidity, mHumidity) == 0;

        }

        @Override
        public int hashCode() {
            int result = (mDayTemp != +0.0f ? Float.floatToIntBits(mDayTemp) : 0);
            result = 31 * result + (mMorningTemp != +0.0f ? Float.floatToIntBits(mMorningTemp) : 0);
            result = 31 * result + (mEveningTemp != +0.0f ? Float.floatToIntBits(mEveningTemp) : 0);
            result = 31 * result + (mNightTemp != +0.0f ? Float.floatToIntBits(mNightTemp) : 0);
            result = 31 * result + (mPressure != +0.0f ? Float.floatToIntBits(mPressure) : 0);
            result = 31 * result + (mHumidity != +0.0f ? Float.floatToIntBits(mHumidity) : 0);
            return result;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            WeatherDescription that = (WeatherDescription) o;

            if (mId != that.mId) return false;
            if (mMain != null ? !mMain.equals(that.mMain) : that.mMain != null) return false;
            if (mDetail != null ? !mDetail.equals(that.mDetail) : that.mDetail != null)
                return false;
            return mIcon != null ? mIcon.equals(that.mIcon) : that.mIcon == null;

        }

        @Override
        public int hashCode() {
            int result = mId;
            result = 31 * result + (mMain != null ? mMain.hashCode() : 0);
            result = 31 * result + (mDetail != null ? mDetail.hashCode() : 0);
            result = 31 * result + (mIcon != null ? mIcon.hashCode() : 0);
            return result;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Wind wind = (Wind) o;

            if (Float.compare(wind.mSpeed, mSpeed) != 0) return false;
            return Float.compare(wind.mDeg, mDeg) == 0;

        }

        @Override
        public int hashCode() {
            int result = (mSpeed != +0.0f ? Float.floatToIntBits(mSpeed) : 0);
            result = 31 * result + (mDeg != +0.0f ? Float.floatToIntBits(mDeg) : 0);
            return result;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Clouds clouds = (Clouds) o;

            return mCloudiness == clouds.mCloudiness;

        }

        @Override
        public int hashCode() {
            return mCloudiness;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Sys sys = (Sys) o;

            if (mSunrise != sys.mSunrise) return false;
            return mSunset == sys.mSunset;

        }

        @Override
        public int hashCode() {
            int result = (int) (mSunrise ^ (mSunrise >>> 32));
            result = 31 * result + (int) (mSunset ^ (mSunset >>> 32));
            return result;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Rain rain = (Rain) o;

            return Float.compare(rain.mVolume3H, mVolume3H) == 0;

        }

        @Override
        public int hashCode() {
            return (mVolume3H != +0.0f ? Float.floatToIntBits(mVolume3H) : 0);
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Snow snow = (Snow) o;

            return Float.compare(snow.mVolume3H, mVolume3H) == 0;

        }

        @Override
        public int hashCode() {
            return (mVolume3H != +0.0f ? Float.floatToIntBits(mVolume3H) : 0);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Weather)) return false;

        Weather weather = (Weather) o;

        if (mDateTime != weather.mDateTime) return false;
        if (mWeatherDescription != null ? !mWeatherDescription.equals(weather.mWeatherDescription)
                : weather.mWeatherDescription != null)
            return false;
        if (mMain != null ? !mMain.equals(weather.mMain) : weather.mMain != null) return false;
        if (mWind != null ? !mWind.equals(weather.mWind) : weather.mWind != null) return false;
        if (mClouds != null ? !mClouds.equals(weather.mClouds) : weather.mClouds != null)
            return false;
        if (mSys != null ? !mSys.equals(weather.mSys) : weather.mSys != null) return false;
        if (mRain != null ? !mRain.equals(weather.mRain) : weather.mRain != null) return false;
        return mSnow != null ? mSnow.equals(weather.mSnow) : weather.mSnow == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (mDateTime ^ (mDateTime >>> 32));
        result = 31 * result + (mWeatherDescription != null ? mWeatherDescription.hashCode() : 0);
        result = 31 * result + (mMain != null ? mMain.hashCode() : 0);
        result = 31 * result + (mWind != null ? mWind.hashCode() : 0);
        result = 31 * result + (mClouds != null ? mClouds.hashCode() : 0);
        result = 31 * result + (mSys != null ? mSys.hashCode() : 0);
        result = 31 * result + (mRain != null ? mRain.hashCode() : 0);
        result = 31 * result + (mSnow != null ? mSnow.hashCode() : 0);
        return result;
    }
}
