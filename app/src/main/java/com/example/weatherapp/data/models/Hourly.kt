package com.example.weatherapp.data.models


import com.google.gson.annotations.SerializedName

data class Hourly(
    @SerializedName("pressure_msl")
    val pressureMsl: List<Double?>?,
    @SerializedName("relative_humidity_2m")
    val relativeHumidity2m: List<Int?>?,
    @SerializedName("temperature_2m")
    val temperature2m: List<Double?>?,
    @SerializedName("time")
    val time: List<String?>?,
    @SerializedName("weather_code")
    val weatherCode: List<Int?>?,
    @SerializedName("wind_speed_10m")
    val windSpeed10m: List<Double?>?
)