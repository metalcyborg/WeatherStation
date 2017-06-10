package com.metalcyborg.weather.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

public class City implements Parcelable {

    private String mOpenWeatherId;
    private String mName;
    private String mCountry;
    private float mLatitude;
    private float mLongitude;
    private String mTimeZone;

    public City(String openWeatherId, String name, String country, float latitude, float longitude,
                @Nullable String timeZone) {
        mOpenWeatherId = openWeatherId;
        mName = name;
        mCountry = country;
        mLatitude = latitude;
        mLongitude = longitude;
        mTimeZone = timeZone;
    }

    protected City(Parcel in) {
        mOpenWeatherId = in.readString();
        mName = in.readString();
        mCountry = in.readString();
        mLatitude = in.readFloat();
        mLongitude = in.readFloat();
        mTimeZone = in.readString();
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    public String getOpenWeatherId() {
        return mOpenWeatherId;
    }

    public String getName() {
        return mName;
    }

    public String getCountry() {
        return mCountry;
    }

    public float getLatitude() {
        return mLatitude;
    }

    public float getLongitude() {
        return mLongitude;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;

        City city = (City) o;

        if (Float.compare(city.mLatitude, mLatitude) != 0) return false;
        if (Float.compare(city.mLongitude, mLongitude) != 0) return false;
        if (!mOpenWeatherId.equals(city.mOpenWeatherId)) return false;
        if (!mName.equals(city.mName)) return false;
        if (!mCountry.equals(city.mCountry)) return false;
        return mTimeZone != null ? mTimeZone.equals(city.mTimeZone) : city.mTimeZone == null;

    }

    @Override
    public int hashCode() {
        int result = mOpenWeatherId.hashCode();
        result = 31 * result + mName.hashCode();
        result = 31 * result + mCountry.hashCode();
        result = 31 * result + (mLatitude != +0.0f ? Float.floatToIntBits(mLatitude) : 0);
        result = 31 * result + (mLongitude != +0.0f ? Float.floatToIntBits(mLongitude) : 0);
        result = 31 * result + (mTimeZone != null ? mTimeZone.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mOpenWeatherId);
        dest.writeString(mName);
        dest.writeString(mCountry);
        dest.writeFloat(mLatitude);
        dest.writeFloat(mLongitude);
        dest.writeString(mTimeZone);
    }
}
