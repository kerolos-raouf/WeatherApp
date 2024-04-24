package com.example.weatherapp.data.networking

import com.example.weatherapp.data.models.WeatherItem

class FakeWeatherData {

    private val fakeData = listOf(
        WeatherItem(1000.1,30,30.7,"15:00",1,10.2),
        WeatherItem(1000.1,30,30.7,"15:00",2,10.2),
        WeatherItem(1000.1,30,30.7,"15:00",3,10.2),
        WeatherItem(1000.1,30,30.7,"15:00",55,10.2),
        WeatherItem(1000.1,30,30.7,"15:00",81,10.2),
        WeatherItem(1000.1,30,30.7,"15:00",2,10.2),
        WeatherItem(1000.1,30,30.7,"15:00",3,10.2),
        WeatherItem(1000.1,30,30.7,"15:00",0,10.2),
        WeatherItem(1000.1,30,30.7,"15:00",45,10.2),
        WeatherItem(1000.1,30,30.7,"15:00",56,10.2),
        WeatherItem(1000.1,30,30.7,"15:00",1,10.2),
    )

    fun getFakeData() : List<WeatherItem> = fakeData
}