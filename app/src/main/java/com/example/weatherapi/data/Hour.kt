package com.example.weatherapi.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Hour(
    @SerializedName("time")
    @Expose
    val time:String? = null,

    @SerializedName("condition")
    @Expose
    val condition: Condition? = null,

    @SerializedName("temp_c")
    @Expose
    val temp_c: String = ""
)
