package com.example.weatherapp.di

import com.example.weatherapp.data.repository.RepositoryImpl
import com.example.weatherapp.domain.networking.WeatherApi
import com.example.weatherapp.domain.repository.Repository
import com.example.weatherapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    fun provideText() : String
    {
        return "it is working."
    }

    @Provides
    fun provideRepository(weatherApi: WeatherApi) : Repository{
        return RepositoryImpl(weatherApi)
    }


    @Provides
    fun provideRetrofit() : Retrofit
    {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Provides
    fun provideWeatherApi(retrofit: Retrofit) : WeatherApi
    {
        return retrofit.create(WeatherApi::class.java)
    }

}