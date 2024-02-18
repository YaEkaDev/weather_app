package com.example.weatherapi.api

import com.example.weatherapi.data.WeatherResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService
{
    @GET("forecast.json")
    fun getWeatherInfo(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = "4fc0b9fe825340bdb2b120750242901",
        @Query(QUERY_PARAM_CITY) city: String,
        @Query(QUERY_PARAM_DAYS) days: Int = 7,
        @Query(QUERY_PARAM_AQI) aqi: String = "no",
        @Query(QUERY_PARAM_ALERTS) alerts: String = "no"
    ): Single<WeatherResponse>

    companion object{
        private const val QUERY_PARAM_API_KEY = "key"
        private const val QUERY_PARAM_CITY = "q"
        private const val QUERY_PARAM_DAYS = "days"
        private const val QUERY_PARAM_AQI = "aqi"
        private const val QUERY_PARAM_ALERTS = "alerts"
    }
}