package com.metalcyborg.weather.data;

import com.google.common.base.Objects;

/**
 * Created by metalcyborg on 17.04.17.
 */

public class City {

    private String mOpenWeatherId;
    private String mName;
    private String mCountry;

    public City(String openWeatherId, String name, String country) {
        mOpenWeatherId = openWeatherId;
        mName = name;
        mCountry = country;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        if (!mOpenWeatherId.equals(city.mOpenWeatherId)) return false;
        if (!mName.equals(city.mName)) return false;
        return mCountry.equals(city.mCountry);

    }

    @Override
    public int hashCode() {
        int result = mOpenWeatherId.hashCode();
        result = 31 * result + mName.hashCode();
        result = 31 * result + mCountry.hashCode();
        return result;
    }
}
