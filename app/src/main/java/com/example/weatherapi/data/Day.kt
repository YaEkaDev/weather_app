package com.example.weatherapi.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Day(
    @SerializedName("maxtemp_c")
    @Expose
    val maxtemp_c:String = "",

    @SerializedName("mintemp_c")
    @Expose
    val mintemp_c:String = "",

    @SerializedName("condition")
    @Expose
    val condition:Condition? = null
)
