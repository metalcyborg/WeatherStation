package com.metalcyborg.weather.data;

public class City {

    private String mOpenWeatherId;
    private String mName;
    private String mCountry;
    private float mLatitude;
    private float mLongitude;
    private String mTimeZone;

    public City(String openWeatherId, String name, String country, float latitude, float longitude,
                String timeZone) {
        mOpenWeatherId = openWeatherId;
        mName = name;
        mCountry = country;
        mLatitude = latitude;
        mLongitude = longitude;
        mTimeZone = timeZone;
    }

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
}
