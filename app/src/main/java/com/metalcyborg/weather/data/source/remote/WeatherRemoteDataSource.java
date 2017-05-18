package com.metalcyborg.weather.data.source.remote;

import com.metalcyborg.weather.data.Weather;
import com.metalcyborg.weather.data.source.remote.models.CurrentWeather;
import com.metalcyborg.weather.data.source.remote.models.Forecast13Days;
import com.metalcyborg.weather.data.source.remote.models.Forecast3Hours;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherRemoteDataSource implements RemoteDataSource {

    private static volatile WeatherRemoteDataSource mInstance;
    private static final String API_KEY = "1d948a9f3798dae877b6b919f0301bd5";
    private static final String UNITS = "metric";
    private static final int FORECAST_3_H_COUNT = 10;
    private Retrofit mRetrofit;
    private CurrentWeatherService mCurrentWeatherService;
    private Forecast3HService mForecast3HoursService;
    private Forecast13DService mForecast13DService;

    private WeatherRemoteDataSource() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mCurrentWeatherService = mRetrofit.create(CurrentWeatherService.class);
        mForecast3HoursService = mRetrofit.create(Forecast3HService.class);
        mForecast13DService = mRetrofit.create(Forecast13DService.class);
    }

    public static WeatherRemoteDataSource getInstance() {
        if (mInstance == null) {
            synchronized (WeatherRemoteDataSource.class) {
                if (mInstance == null) {
                    mInstance = new WeatherRemoteDataSource();
                }
            }
        }

        return mInstance;
    }

    private Weather generateWeatherObject(CurrentWeather currentWeather) {

        Weather weather = new Weather(currentWeather.getDateTime());

        if (currentWeather.getMain() != null) {
            Weather.Main main = new Weather.Main(
                    currentWeather.getMain().getTemp(),
                    0, 0, 0,
                    currentWeather.getMain().getPressure(),
                    currentWeather.getMain().getHumidity()
            );
            weather.setMain(main);
        }

        if (currentWeather.getWeatherDescription() != null &&
                currentWeather.getWeatherDescription().get(0) != null) {
            Weather.WeatherDescription description = new Weather.WeatherDescription(
                    currentWeather.getWeatherDescription().get(0).getId(),
                    currentWeather.getWeatherDescription().get(0).getMain(),
                    currentWeather.getWeatherDescription().get(0).getDetail(),
                    currentWeather.getWeatherDescription().get(0).getIcon()
            );
            weather.setWeatherDescription(description);
        }

        if (currentWeather.getClouds() != null) {
            Weather.Clouds clouds = new Weather.Clouds(currentWeather.getClouds().getCloudiness());
            weather.setClouds(clouds);
        }

        if (currentWeather.getWind() != null) {
            Weather.Wind wind = new Weather.Wind(
                    currentWeather.getWind().getSpeed(),
                    currentWeather.getWind().getDeg()
            );
            weather.setWind(wind);
        }

        if (currentWeather.getRain() != null) {
            Weather.Rain rain = new Weather.Rain(currentWeather.getRain().getVolume3H());
            weather.setRain(rain);
        }

        if (currentWeather.getSnow() != null) {
            Weather.Snow snow = new Weather.Snow(currentWeather.getSnow().getVolume3H());
            weather.setSnow(snow);
        }

        if (currentWeather.getSys() != null) {
            Weather.Sys sys = new Weather.Sys(
                    currentWeather.getSys().getSunrise(),
                    currentWeather.getSys().getSunset()
            );
            weather.setSys(sys);
        }

        return weather;
    }

    private List<Weather> generate3HForecastList(Forecast3Hours forecast3Hours) {
        List<Weather> forecast = new ArrayList<>();

        for (Forecast3Hours.Weather forecastWeather : forecast3Hours.getWeatherList()) {
            Weather weather = new Weather(forecastWeather.getDateTime());

            if (forecastWeather.getMain() != null) {
                Weather.Main main = new Weather.Main(
                        forecastWeather.getMain().getTemp(),
                        0, 0, 0,
                        forecastWeather.getMain().getPressure(),
                        forecastWeather.getMain().getHumidity()
                );
                weather.setMain(main);
            }
            if (forecastWeather.getWeatherDescription() != null &&
                    forecastWeather.getWeatherDescription().get(0) != null) {
                Weather.WeatherDescription description = new Weather.WeatherDescription(
                        forecastWeather.getWeatherDescription().get(0).getId(),
                        forecastWeather.getWeatherDescription().get(0).getMain(),
                        forecastWeather.getWeatherDescription().get(0).getDetail(),
                        forecastWeather.getWeatherDescription().get(0).getIcon()
                );
                weather.setWeatherDescription(description);
            }

            if (forecastWeather.getClouds() != null) {
                Weather.Clouds clouds = new Weather.Clouds(
                        forecastWeather.getClouds().getCloudiness());
                weather.setClouds(clouds);
            }

            if (forecastWeather.getWind() != null) {
                Weather.Wind wind = new Weather.Wind(
                        forecastWeather.getWind().getSpeed(),
                        forecastWeather.getWind().getDeg()
                );
                weather.setWind(wind);
            }

            if (forecastWeather.getRain() != null) {
                Weather.Rain rain = new Weather.Rain(forecastWeather.getRain().getVolume3H());
                weather.setRain(rain);
            }

            if (forecastWeather.getSnow() != null) {
                Weather.Snow snow = new Weather.Snow(forecastWeather.getSnow().getVolume3H());
                weather.setSnow(snow);
            }

            forecast.add(weather);
        }

        return forecast;
    }

    private List<Weather> generate13DForecastList(Forecast13Days forecast13Days) {
        List<Weather> forecast = new ArrayList<>();

        for (Forecast13Days.Weather forecastWeather : forecast13Days.getWeatherList()) {
            Weather weather = new Weather(forecastWeather.getDateTime());

            if (forecastWeather.getTemp() != null) {
                Weather.Main main = new Weather.Main(
                        forecastWeather.getTemp().getDay(),
                        forecastWeather.getTemp().getMorning(),
                        forecastWeather.getTemp().getEvening(),
                        forecastWeather.getTemp().getNight(),
                        forecastWeather.getPressure(),
                        forecastWeather.getHumidity()
                );
                weather.setMain(main);
            }

            if (forecastWeather.getWeatherDescription() != null &&
                    forecastWeather.getWeatherDescription().get(0) != null) {
                Weather.WeatherDescription description = new Weather.WeatherDescription(
                        forecastWeather.getWeatherDescription().get(0).getId(),
                        forecastWeather.getWeatherDescription().get(0).getMain(),
                        forecastWeather.getWeatherDescription().get(0).getDetail(),
                        forecastWeather.getWeatherDescription().get(0).getIcon()
                );
                weather.setWeatherDescription(description);
            }

            Weather.Clouds clouds = new Weather.Clouds(
                    forecastWeather.getClouds());
            weather.setClouds(clouds);

            Weather.Wind wind = new Weather.Wind(
                    forecastWeather.getWindSpeed(),
                    forecastWeather.getWindDeg()
            );
            weather.setWind(wind);

            Weather.Rain rain = new Weather.Rain(forecastWeather.getRain());
            weather.setRain(rain);

            Weather.Snow snow = new Weather.Snow(forecastWeather.getSnow());
            weather.setSnow(snow);

            forecast.add(weather);
        }

        return forecast;
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
    public void load3HForecastData(String cityId, final GetForecastCallback callback) {
        Call<Forecast3Hours> forecastCall = mForecast3HoursService
                .forecast3H(cityId, API_KEY, UNITS, FORECAST_3_H_COUNT);
        forecastCall.enqueue(new Callback<Forecast3Hours>() {
            @Override
            public void onResponse(Call<Forecast3Hours> call, Response<Forecast3Hours> response) {
                Forecast3Hours forecast3Hours = response.body();
                List<Weather> forecast = generate3HForecastList(forecast3Hours);
                callback.onDataLoaded(forecast);
            }

            @Override
            public void onFailure(Call<Forecast3Hours> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void load13DForecastData(String cityId, final GetForecastCallback callback) {
        Call<Forecast13Days> forecastCall = mForecast13DService
                .forecast13D(cityId, API_KEY, UNITS, 14);
        forecastCall.enqueue(new Callback<Forecast13Days>() {
            @Override
            public void onResponse(Call<Forecast13Days> call, Response<Forecast13Days> response) {
                Forecast13Days forecast13Days = response.body();
                List<Weather> forecast = generate13DForecastList(forecast13Days);
                callback.onDataLoaded(forecast);
            }

            @Override
            public void onFailure(Call<Forecast13Days> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }
}
