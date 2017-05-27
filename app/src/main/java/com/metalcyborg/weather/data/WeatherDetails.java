package com.metalcyborg.weather.data;

import android.os.Parcel;
import android.os.Parcelable;

public class WeatherDetails implements Parcelable {

    private float mTemperature;
    private float mPressure;
    private float mHumidity;
    private float mWindSpeed;
    private float mWindDeg;
    private long mSunrise;
    private long mSunset;
    private String mIcon;

    public WeatherDetails() {

    }

    public WeatherDetails(float temperature, float pressure, float humidity, float windSpeed,
                          int windDeg, long sunrise, long sunset, String icon) {
        mTemperature = temperature;
        mPressure = pressure;
        mHumidity = humidity;
        mWindSpeed = windSpeed;
        mWindDeg = windDeg;
        mSunrise = sunrise;
        mSunset = sunset;
        mIcon = icon;
    }

    protected WeatherDetails(Parcel in) {
        mTemperature = in.readFloat();
        mPressure = in.readFloat();
        mHumidity = in.readFloat();
        mWindSpeed = in.readFloat();
        mWindDeg = in.readFloat();
        mSunrise = in.readLong();
        mSunset = in.readLong();
        mIcon = in.readString();
    }

    public static final Creator<WeatherDetails> CREATOR = new Creator<WeatherDetails>() {
        @Override
        public WeatherDetails createFromParcel(Parcel in) {
            return new WeatherDetails(in);
        }

        @Override
        public WeatherDetails[] newArray(int size) {
            return new WeatherDetails[size];
        }
    };

    public float getTemperature() {
        return mTemperature;
    }

    public float getPressure() {
        return mPressure;
    }

    public float getHumidity() {
        return mHumidity;
    }

    public float getWindSpeed() {
        return mWindSpeed;
    }

    public float getWindDeg() {
        return mWindDeg;
    }

    public long getSunrise() {
        return mSunrise;
    }

    public long getSunset() {
        return mSunset;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setTemperature(float temperature) {
        mTemperature = temperature;
    }

    public void setPressure(float pressure) {
        mPressure = pressure;
    }

    public void setHumidity(float humidity) {
        mHumidity = humidity;
    }

    public void setWindSpeed(float windSpeed) {
        mWindSpeed = windSpeed;
    }

    public void setWindDeg(float windDeg) {
        mWindDeg = windDeg;
    }

    public void setSunrise(long sunrise) {
        mSunrise = sunrise;
    }

    public void setSunset(long sunset) {
        mSunset = sunset;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(mTemperature);
        dest.writeFloat(mPressure);
        dest.writeFloat(mHumidity);
        dest.writeFloat(mWindSpeed);
        dest.writeFloat(mWindDeg);
        dest.writeLong(mSunrise);
        dest.writeLong(mSunset);
        dest.writeString(mIcon);
    }
}
