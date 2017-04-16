package com.metalcyborg.weather.data.source;

import android.support.annotation.NonNull;

import com.metalcyborg.weather.data.Weather;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by root on 14.04.17.
 */

public class WeatherRepository implements WeatherDataSource {

    private static volatile WeatherRepository mInstance;
    private WeatherDataSource mLocalDataSource;
    private WeatherDataSource mRemoteDataSource;

    private WeatherRepository(@NonNull WeatherDataSource localDataSource,
                              @NonNull WeatherDataSource remoteDataSource) {
        mLocalDataSource = checkNotNull(localDataSource);
        mRemoteDataSource = checkNotNull(remoteDataSource);
    }

    public static WeatherRepository getInstance(WeatherDataSource localDataSource,
                                         WeatherDataSource remoteDataSource) {
        if(mInstance == null) {
            synchronized (WeatherRepository.class) {
                if(mInstance == null) {
                    mInstance = new WeatherRepository(localDataSource, remoteDataSource);
                }
            }
        }

        return mInstance;
    }
}
