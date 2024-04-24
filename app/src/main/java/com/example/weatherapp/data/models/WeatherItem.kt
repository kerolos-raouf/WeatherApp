package com.example.weatherapp.data.models

import com.google.gson.annotations.SerializedName

data class WeatherItem(
    val pressureMsl: Double?,
    val relativeHumidity2m: Int?,
    val temperature2m: Double?,
    val time: String?,
    val weatherCode: Int?,
    val windSpeed10m: Double?
)
