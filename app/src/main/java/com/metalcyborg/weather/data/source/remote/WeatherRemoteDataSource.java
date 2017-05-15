package com.metalcyborg.weather.data.source.remote;

import com.metalcyborg.weather.data.Weather;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherRemoteDataSource implements RemoteDataSource {

    private static volatile WeatherRemoteDataSource mInstance;
    private static final String API_KEY = "1d948a9f3798dae877b6b919f0301bd5";
    private static final String UNITS = "metric";
    private Retrofit mRetrofit;
    private CurrentWeatherService mCurrentWeatherService;

    private WeatherRemoteDataSource() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mCurrentWeatherService = mRetrofit.create(CurrentWeatherService.class);
    }

    public static WeatherRemoteDataSource getInstance() {
        if(mInstance == null) {
            synchronized (WeatherRemoteDataSource.class) {
                if(mInstance == null) {
                    mInstance = new WeatherRemoteDataSource();
                }
            }
        }

        return mInstance;
    }

    @Override
    public void loadWeatherData(final String cityId, final GetWeatherCallback callback) {
        Call<Weather> currentWeatherModelCall = mCurrentWeatherService
                .currentWeather(cityId, API_KEY, UNITS);
        currentWeatherModelCall.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call,
                                   Response<Weather> response) {
                Weather currentWeather = response.body();

                callback.onDataLoaded(cityId, currentWeather);
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                callback.onDataNotAvailable(cityId);
            }
        });
    }
}
