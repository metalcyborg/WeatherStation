package com.metalcyborg.weather;


import android.content.Context;

import com.metalcyborg.weather.data.source.local.LocalDataSource;
import com.metalcyborg.weather.data.source.local.WeatherLocalDataSource;
import com.metalcyborg.weather.data.source.remote.RemoteDataSource;
import com.metalcyborg.weather.data.source.remote.WeatherRemoteDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class WeatherRepositoryModule {

    @Provides
    @Singleton
    public LocalDataSource provideLocalDataSource(Context context) {
        return new WeatherLocalDataSource(context);
    }

    @Provides
    @Singleton
    public RemoteDataSource provideRemoteDataSource() {
        return new WeatherRemoteDataSource();
    }
}
