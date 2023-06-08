package com.example.pakaianapanich.data

import android.content.ContentValues.TAG
import android.util.Log
import com.example.pakaianapanich.data.source.remote.api.ApiService
import com.example.pakaianapanich.data.source.remote.response.WeatherResponse
import com.example.pakaianapanich.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.http.Query
import java.lang.Exception

class UserRepository(private val apiService: ApiService) {

    suspend fun getWeather(
        @Query("lat") lat: String,
        @Query("lon") long: String,
    ): Flow<Resource<WeatherResponse>> {
        return flow {
            emit(Resource.Loading)
            try {
                val response = apiService.getCurrentWeather(lat, long)
                emit(Resource.Success(response))
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
                Log.e(TAG, "userRepository getWeather exception: ${e.message}")
            }
        }
    }

}