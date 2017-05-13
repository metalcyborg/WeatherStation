package com.metalcyborg.weather.data;

/**
 * Created by metalcyborg on 17.04.17.
 */

public class City {

    private String mOpenWeatherId;
    private String mName;
    private String mCountry;
    private long mLon;
    private long mLat;

    public City(String openWeatherId, String name, String country, long lon, long lat) {
        mOpenWeatherId = openWeatherId;
        mName = name;
        mCountry = country;
        mLon = lon;
        mLat = lat;
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

    public long getLon() {
        return mLon;
    }

    public long getLat() {
        return mLat;
    }
}
