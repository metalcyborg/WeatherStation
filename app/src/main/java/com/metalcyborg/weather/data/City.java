package com.metalcyborg.weather.data;

/**
 * Created by metalcyborg on 17.04.17.
 */

public class City {

    private String mId;
    private String mOpenweatherId;
    private String mName;
    private String mCountry;
    private long mLon;
    private long mLat;

    public City(String id, String openweatherId, String name, String country, long lon, long lat) {
        mId = id;
        mOpenweatherId = openweatherId;
        mName = name;
        mCountry = country;
        mLon = lon;
        mLat = lat;
    }

    public String getId() {
        return mId;
    }

    public String getOpenweatherId() {
        return mOpenweatherId;
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
