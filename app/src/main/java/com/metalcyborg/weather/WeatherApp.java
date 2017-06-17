package com.metalcyborg.weather;

import android.app.Application;

import com.metalcyborg.weather.data.source.DaggerWeatherRepositoryComponent;
import com.metalcyborg.weather.data.source.WeatherRepositoryComponent;

public class WeatherApp extends Application {

    private WeatherRepositoryComponent mWeatherRepositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mWeatherRepositoryComponent = DaggerWeatherRepositoryComponent.builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .build();
    }

    public WeatherRepositoryComponent getWeatherRepositoryComponent() {
        return mWeatherRepositoryComponent;
    }
}
