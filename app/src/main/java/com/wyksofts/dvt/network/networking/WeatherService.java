package com.wyksofts.dvt.network.networking;
import com.wyksofts.dvt.model.todayweather.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("https://api.openweathermap.org/data/2.5/weather?")
    Call<WeatherResponse> getCurrentWeatherData(@Query("lat") double lat,
                                                @Query("lon") double lon,
                                                @Query("APPID") String app_id);

    @GET("https://api.openweathermap.org/data/2.5/forecast?")
    Call<WeatherResponse> getCurrentForecastData(@Query("lat") double lat,
                                                 @Query("lon") double lon,
                                                 @Query("APPID") String app_id);
}
