package com.example.weatherapi.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Astro(
    @SerializedName("sunrise")
    @Expose
    val sunrise: String? = null,

    @SerializedName("sunset")
    @Expose
    val sunset: String? = null,

    @SerializedName("moonrise")
    @Expose
    val moonrise: String? = null,

    @SerializedName("moonset")
    @Expose
    val moonset: String? = null
)
