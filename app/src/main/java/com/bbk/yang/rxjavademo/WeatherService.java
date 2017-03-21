package com.bbk.yang.rxjavademo;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by yang on 2017/3/21.
 */

public interface WeatherService {
    @GET("now.json")
    Call<WeatherLiveBean> getWeatherLive(@Query("key") String key, @Query("location") String location);
}
