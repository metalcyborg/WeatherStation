package com.metalcyborg.weather.data.source.remote;

import com.metalcyborg.weather.data.source.remote.models.CurrentWeather;
import com.metalcyborg.weather.data.source.remote.models.Forecast3Hours;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by metalcyborg on 14.05.17.
 */

public interface Forecast3HService {
    @GET("data/2.5/forecast")
    Call<Forecast3Hours> forecast3H(@Query("id") String cityId,
                                        @Query("appid") String apiKey,
                                        @Query("units") String units,
                                        @Query("cnt") int count);
}
