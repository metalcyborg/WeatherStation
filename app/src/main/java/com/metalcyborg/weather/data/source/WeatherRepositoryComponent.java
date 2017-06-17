package com.metalcyborg.weather.data.source;


import android.content.Context;

import com.metalcyborg.weather.ApplicationModule;
import com.metalcyborg.weather.WeatherRepositoryModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {WeatherRepositoryModule.class, ApplicationModule.class})
public interface WeatherRepositoryComponent {

    WeatherRepository getWeatherRepository();
    Context getContext();
}
