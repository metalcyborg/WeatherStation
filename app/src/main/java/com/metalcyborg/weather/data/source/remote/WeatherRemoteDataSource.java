package com.metalcyborg.weather.data.source.remote;

import com.metalcyborg.weather.data.Weather;
import com.metalcyborg.weather.data.source.remote.models.CurrentWeather;

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

    private Weather generateWeatherObject(CurrentWeather currentWeather) {

        Weather weather = new Weather(currentWeather.getDateTime());

        if(currentWeather.getMain() != null) {
            Weather.Main main = new Weather.Main(
                    currentWeather.getMain().getTemp(),
                    currentWeather.getMain().getPressure(),
                    currentWeather.getMain().getHumidity()
            );
            weather.setMain(main);
        }

        if(currentWeather.getWeatherDescription() != null) {
            Weather.WeatherDescription description = new Weather.WeatherDescription(
                    currentWeather.getWeatherDescription().get(0).getId(),
                    currentWeather.getWeatherDescription().get(0).getMain(),
                    currentWeather.getWeatherDescription().get(0).getDetail(),
                    currentWeather.getWeatherDescription().get(0).getIcon()
            );
            weather.setWeatherDescription(description);
        }

        if(currentWeather.getClouds() != null) {
            Weather.Clouds clouds = new Weather.Clouds(currentWeather.getClouds().getCloudiness());
            weather.setClouds(clouds);
        }

        if(currentWeather.getWind() != null) {
            Weather.Wind wind = new Weather.Wind(
                    currentWeather.getWind().getSpeed(),
                    currentWeather.getWind().getDeg()
            );
            weather.setWind(wind);
        }

        if(currentWeather.getRain() != null) {
            Weather.Rain rain = new Weather.Rain(currentWeather.getRain().getVolume3H());
            weather.setRain(rain);
        }

        if(currentWeather.getSnow() != null) {
            Weather.Snow snow = new Weather.Snow(currentWeather.getSnow().getVolume3H());
            weather.setSnow(snow);
        }

        if(currentWeather.getSys() != null) {
            Weather.Sys sys = new Weather.Sys(
                    currentWeather.getSys().getSunrise(),
                    currentWeather.getSys().getSunset()
            );
            weather.setSys(sys);
        }

        return weather;
    }

    @Override
    public void loadWeatherData(final String cityId, final GetWeatherCallback callback) {
        Call<CurrentWeather> currentWeatherModelCall = mCurrentWeatherService
                .currentWeather(cityId, API_KEY, UNITS);
        currentWeatherModelCall.enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(Call<CurrentWeather> call,
                                   Response<CurrentWeather> response) {
                CurrentWeather currentWeather = response.body();
                callback.onDataLoaded(cityId, generateWeatherObject(currentWeather));
            }

            @Override
            public void onFailure(Call<CurrentWeather> call, Throwable t) {
                callback.onDataNotAvailable(cityId);
            }
        });
    }

    @Override
    public void load3HForecastData(String cityId, GetForecastCallback callback) {

    }

    @Override
    public void load13DForecastData(String cityId, GetForecastCallback callback) {

    }
}
