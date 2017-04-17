package com.metalcyborg.weather.data;

/**
 * Created by metalcyborg on 17.04.17.
 */

public class City {

    private String mId;
    private String mName;
    private String mCountry;
    private long mLon;
    private long mLat;

    public City(String id, String name, String country, long lon, long lat) {
        mId = id;
        mName = name;
        mCountry = country;
        mLon = lon;
        mLat = lat;
    }
}
