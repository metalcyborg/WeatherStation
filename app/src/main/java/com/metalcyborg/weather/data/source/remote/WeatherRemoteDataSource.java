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
        Call<CurrentWeatherModel> currentWeatherModelCall = mCurrentWeatherService
                .currentWeather(cityId, API_KEY);
        currentWeatherModelCall.enqueue(new Callback<CurrentWeatherModel>() {
            @Override
            public void onResponse(Call<CurrentWeatherModel> call,
                                   Response<CurrentWeatherModel> response) {
                CurrentWeatherModel currentWeatherModel = response.body();
                Weather weather = new Weather();
                weather.setTemperature(currentWeatherModel.getMain().getTemp());

                callback.onDataLoaded(cityId, weather);
            }

            @Override
            public void onFailure(Call<CurrentWeatherModel> call, Throwable t) {
                callback.onDataNotAvailable(cityId);
            }
        });
    }
}
