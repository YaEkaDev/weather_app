package com.example.weatherapi.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("location")
    @Expose
    val location: Location? = null,

    @SerializedName("current")
    @Expose
    val current: Current? = null,

    @SerializedName("forecast")
    @Expose
    val forecast: Forecast? = null


)
