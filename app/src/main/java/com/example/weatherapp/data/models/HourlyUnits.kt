package com.example.weatherapp.data.models


import com.google.gson.annotations.SerializedName

data class HourlyUnits(
    @SerializedName("pressure_msl")
    val pressureMsl: String?,
    @SerializedName("relative_humidity_2m")
    val relativeHumidity2m: String?,
    @SerializedName("temperature_2m")
    val temperature2m: String?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("weather_code")
    val weatherCode: String?,
    @SerializedName("wind_speed_10m")
    val windSpeed10m: String?
)