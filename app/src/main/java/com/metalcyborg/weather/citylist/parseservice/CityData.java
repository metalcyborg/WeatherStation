package com.metalcyborg.weather.citylist.parseservice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by metalcyborg on 30.04.17.
 */

public class CityData {

    @SerializedName("_id")
    private int mId;

    @SerializedName("name")
    private String mCityName;

    @SerializedName("country")
    private String mCountryName;

    @SerializedName("coord")
    private Coord mCoord;

    public int getId() {
        return mId;
    }

    public String getCityName() {
        return mCityName;
    }

    public String getCountryName() {
        return mCountryName;
    }

    public Coord getCoord() {
        return mCoord;
    }

    public class Coord {

        @SerializedName("lon")
        private double mLon;

        @SerializedName("lat")
        private double mLat;

        public double getLon() {
            return mLon;
        }

        public double getLat() {
            return mLat;
        }
    }
}
