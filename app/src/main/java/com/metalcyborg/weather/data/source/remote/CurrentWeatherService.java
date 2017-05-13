package com.metalcyborg.weather.data.source.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by metalcyborg on 14.05.17.
 */

public interface CurrentWeatherService {
    @GET("data/2.5/weather")
    Call<CurrentWeatherModel> currentWeather(@Query("id") String cityId,
                                             @Query("APPID") String apiKey);
}
