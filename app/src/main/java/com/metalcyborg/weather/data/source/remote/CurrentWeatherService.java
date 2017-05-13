package com.metalcyborg.weather.data.source.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by metalcyborg on 14.05.17.
 */

public interface CurrentWeatherService {
    @GET("data/2.5/weather?id={city_id}&APPID={api_key}")
    Call<CurrentWeatherModel> currentWeather(@Path("city_id") String cityId,
                                             @Path("api_key") String apiKey);
}
