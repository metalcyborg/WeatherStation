package com.metalcyborg.weather.data;

/**
 * Created by metalcyborg on 08.05.17.
 */

public class CityWeather {

    private City mCity;
    private Weather mWeather;

    public CityWeather(City city, Weather weather) {
        mCity = city;
        mWeather = weather;
    }

    public City getCity() {
        return mCity;
    }

    public Weather getWeather() {
        return mWeather;
    }

    public void setWeather(Weather weather) {
        mWeather = weather;
    }
}
