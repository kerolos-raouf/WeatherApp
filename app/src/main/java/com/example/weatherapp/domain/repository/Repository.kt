package com.example.weatherapp.domain.repository

import com.example.weatherapp.data.models.WeatherDto
import com.example.weatherapp.data.models.WeatherItem
import com.example.weatherapp.domain.networking.State
import com.example.weatherapp.domain.networking.WeatherApi
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getWeather(lat : Double,long: Double) : Flow<State<List<WeatherItem>?>>
    fun getFakeData() : List<WeatherItem>
}