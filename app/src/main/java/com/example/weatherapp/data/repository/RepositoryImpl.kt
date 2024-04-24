package com.example.weatherapp.data.repository

import com.example.weatherapp.data.models.WeatherDto
import com.example.weatherapp.data.models.WeatherItem
import com.example.weatherapp.data.networking.FakeWeatherData
import com.example.weatherapp.domain.networking.State
import com.example.weatherapp.domain.networking.WeatherApi
import com.example.weatherapp.domain.repository.Repository
import com.example.weatherapp.utils.toWeatherItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
        private val weatherApi : WeatherApi
) : Repository
{
    override fun getWeather(lat : Double,long: Double): Flow<State<List<WeatherItem>?>> {
        return flow {
            emit(State.Loading)
            try {
                val result = weatherApi.getWeatherData(lat,long)
                if(result.isSuccessful)
                {
                    emit(State.Success(toWeatherItem(result.body())))
                }else
                {
                    emit(State.Error(result.message()))
                }
            }catch (ex : Exception)
            {
                emit(State.Error(ex.message.toString()))
            }
        }
    }

    override fun getFakeData(): List<WeatherItem> = FakeWeatherData().getFakeData()

}