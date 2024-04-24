package com.example.weatherapp.utils

import com.example.weatherapp.data.models.WeatherDto
import com.example.weatherapp.data.models.WeatherItem

fun toWeatherItem(weatherDto: WeatherDto?) : List<WeatherItem>{
    val list : ArrayList<WeatherItem> = ArrayList()

    if (weatherDto != null) {
        for (i in 0..((weatherDto.hourly?.weatherCode?.size?.minus(1)) ?: 0)) {
            list.add(
                WeatherItem(
                    weatherDto.hourly?.pressureMsl?.get(i),
                    weatherDto.hourly?.relativeHumidity2m?.get(i),
                    weatherDto.hourly?.temperature2m?.get(i),
                    weatherDto.hourly?.time?.get(i)?.substring(weatherDto.hourly.time[i]!!.length-5,weatherDto.hourly.time[i]!!.length),
                    weatherDto.hourly?.weatherCode?.get(i),
                    weatherDto.hourly?.windSpeed10m?.get(i),
                )
            )
        }
    }

    return list
}