package com.example.weatherapi.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Forecastday(
    @SerializedName("date")
    @Expose
    val date:String? = null,

    @SerializedName("day")
    @Expose
    val day:Day? = null,

    @SerializedName("hour")
    @Expose
    val hour:List<Hour>? = null,

    @SerializedName("astro")
    @Expose
    val astro:Astro? = null

)
