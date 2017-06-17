package com.metalcyborg.weather;

import com.metalcyborg.weather.data.FakeWeatherRemoteDataSource;
import com.metalcyborg.weather.data.source.local.LocalDataSource;
import com.metalcyborg.weather.data.source.local.WeatherLocalDataSource;
import com.metalcyborg.weather.data.source.remote.RemoteDataSource;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class WeatherRepositoryModule {

    @Singleton
    @Binds
    public abstract LocalDataSource provideLocalDataSource(WeatherLocalDataSource dataSource);

    @Singleton
    @Binds
    public abstract RemoteDataSource provideRemoteDataSource(FakeWeatherRemoteDataSource dataSource);
}
