package com.metalcyborg.weather;

import android.content.Context;
import android.support.annotation.NonNull;

import com.metalcyborg.weather.data.source.WeatherRepository;
import com.metalcyborg.weather.data.source.local.WeatherLocalDataSource;
import com.metalcyborg.weather.data.source.remote.WeatherRemoteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by metalcyborg on 16.04.17.
 */

public class Injection {

    public static WeatherRepository provideWeatherRepository(@NonNull Context context) {
        checkNotNull(context);
        return WeatherRepository.getInstance(WeatherLocalDataSource.getInstance(context),
                WeatherRemoteDataSource.getInstance());
    }
}
