package com.metalcyborg.weather.data.source.remote;

import com.metalcyborg.weather.data.source.remote.models.TimeZone;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TimeZoneService {
    @GET("maps/api/timezone/json")
    Call<TimeZone> timeZone(@Query("location") String coordinates,
                            @Query("timestamp") long timeStampInSec,
                            @Query("key") String apiKey);
}
