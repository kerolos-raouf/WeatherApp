package com.example.weatherapp.domain.networking

import com.example.weatherapp.data.models.WeatherDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {


    @GET("v1/forecast?hourly=temperature_2m,weather_code,relative_humidity_2m,wind_speed_10m,pressure_msl")
    suspend fun getWeatherData(
        @Query("latitude") lat : Double,
        @Query("longitude") long: Double
    ) : Response<WeatherDto>
}