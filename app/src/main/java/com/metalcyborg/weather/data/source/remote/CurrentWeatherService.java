package com.metalcyborg.weather.data.source.remote;

import com.metalcyborg.weather.data.source.remote.models.CurrentWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface CurrentWeatherService {
    @GET("data/2.5/weather")
    Call<CurrentWeather> currentWeather(@Query("id") String cityId,
                                        @Query("APPID") String apiKey);
}
