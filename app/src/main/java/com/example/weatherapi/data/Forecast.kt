package com.example.weatherapi.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Forecast(
    @SerializedName("forecastday")
    @Expose
    val forecastday:List<Forecastday> = listOf()
)
