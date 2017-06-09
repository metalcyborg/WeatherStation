package com.metalcyborg.weather.data;

public class City {

    private String mOpenWeatherId;
    private String mName;
    private String mCountry;
    private float mLatitude;
    private float mLongitude;

    public City(String openWeatherId, String name, String country, float latitude, float longitude) {
        mOpenWeatherId = openWeatherId;
        mName = name;
        mCountry = country;
        mLatitude = latitude;
        mLongitude = longitude;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;

        City city = (City) o;

        if (Float.compare(city.mLatitude, mLatitude) != 0) return false;
        if (Float.compare(city.mLongitude, mLongitude) != 0) return false;
        if (mOpenWeatherId != null ? !mOpenWeatherId.equals(city.mOpenWeatherId) : city.mOpenWeatherId != null)
            return false;
        if (mName != null ? !mName.equals(city.mName) : city.mName != null) return false;
        return mCountry != null ? mCountry.equals(city.mCountry) : city.mCountry == null;

    }

    @Override
    public int hashCode() {
        int result = mOpenWeatherId != null ? mOpenWeatherId.hashCode() : 0;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        result = 31 * result + (mCountry != null ? mCountry.hashCode() : 0);
        result = 31 * result + (mLatitude != +0.0f ? Float.floatToIntBits(mLatitude) : 0);
        result = 31 * result + (mLongitude != +0.0f ? Float.floatToIntBits(mLongitude) : 0);
        return result;
    }
}
