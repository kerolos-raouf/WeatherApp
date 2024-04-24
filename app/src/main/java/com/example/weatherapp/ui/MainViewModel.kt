package com.example.weatherapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.models.WeatherItem
import com.example.weatherapp.domain.networking.State
import com.example.weatherapp.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository : Repository
) : ViewModel() {

    private val _weatherItems = MutableLiveData<List<WeatherItem>?>()
    val weatherItems : LiveData<List<WeatherItem>?> = _weatherItems

    var mainItemValues = MutableLiveData<WeatherItem?>()


    //location information
    var longitude = MutableLiveData<Double>(30.8025)
    var latitude = MutableLiveData<Double>(26.8206)
    var countryName = MutableLiveData<String>("Not Located")
    var cityName = MutableLiveData<String>("Not Located")

    // progress bar state
    private val _showProgressBar = MutableLiveData<Boolean>(false)
    val showProgressBar : LiveData<Boolean> = _showProgressBar

    //error message
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage : LiveData<String> = _errorMessage

    init {
        getFakeWeatherItems()
        getDataFromWeatherApi()
    }

    private fun getFakeWeatherItems()
    {
        val itemsList = repository.getFakeData()
        mainItemValues.postValue(itemsList[0])
        _weatherItems.postValue(itemsList)
    }

    fun getDataFromWeatherApi()
    {
        viewModelScope.launch {
            repository.getWeather(latitude.value!!,longitude.value!!).collect{
                //Log.d("Kerolos",it.toString())
                when(it)
                {
                    is State.Success->{
                        mainItemValues.postValue(it.data?.get(0))
                        _weatherItems.postValue(it.data)
                        _showProgressBar.postValue(false)
                    }
                    is State.Loading->{
                        _showProgressBar.postValue(true)
                    }
                    is State.Error->{
                        _errorMessage.postValue(it.message)
                        _showProgressBar.postValue(false)
                    }
                }
            }
        }
    }

}