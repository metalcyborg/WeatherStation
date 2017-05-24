package com.metalcyborg.weather.data.source.remote;

import com.metalcyborg.weather.data.source.remote.models.Forecast13Days;
import com.metalcyborg.weather.data.source.remote.models.Forecast3Hours;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by metalcyborg on 14.05.17.
 */

public interface Forecast13DService {
    @GET("data/2.5/forecast/daily")
    Call<Forecast13Days> forecast13D(@Query("id") String cityId,
                                     @Query("appid") String apiKey,
                                     @Query("cnt") int count);
}
