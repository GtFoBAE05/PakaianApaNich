package com.example.pakaianapanich.ui.homescreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pakaianapanich.data.UserRepository
import com.example.pakaianapanich.data.source.remote.response.WeatherResponse
import com.example.pakaianapanich.utils.Resource
import kotlinx.coroutines.launch

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    private var _weatherCondition = MutableLiveData<Resource<WeatherResponse>>()
    val weatherCondition: LiveData<Resource<WeatherResponse>> = _weatherCondition

    fun getWeather(
        lat: String,
        lon: String
    ) {
        viewModelScope.launch {
            userRepository.getWeather(lat, lon).collect {
                _weatherCondition.postValue(it)
            }
        }
    }

}