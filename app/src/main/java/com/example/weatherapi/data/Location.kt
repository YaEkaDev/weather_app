package com.example.weatherapi.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("name")
    @Expose
    val name: String?=null
)
