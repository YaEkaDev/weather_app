package com.example.weatherapi.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("last_updated")
    @Expose
    val last_updated: String? = null,

    @SerializedName("temp_c")
    @Expose
    val temp_c: String = "",

    @SerializedName("wind_kph")
    @Expose
    val wind_kph: String? = null,

    @SerializedName("condition")
    @Expose
    val condition: Condition? = null,

    @SerializedName("humidity")
    @Expose
    val humidity: String? = null


)
