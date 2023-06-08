package com.example.pakaianapanich.data.source.remote.api

import com.example.pakaianapanich.BuildConfig
import com.example.pakaianapanich.data.source.remote.response.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

val apiKey = BuildConfig.API_KEY

interface ApiService {


    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") long: String,
        @Query("appid") appid: String = apiKey,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}