package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.api.createOpenWeatherMapService

import com.example.weatherapp.api.CurrentWeather
import com.example.weatherapp.api.WeeklyForecast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


import kotlin.random.Random

class ForecastRespository {

    private val _currentWeather = MutableLiveData<CurrentWeather>()
    val currentWeather: LiveData<CurrentWeather> = _currentWeather

    private val _weeklyForecast= MutableLiveData<WeeklyForecast>()
    val weeklyForecast: LiveData<WeeklyForecast> = _weeklyForecast

    fun loadCurrentForecast(zipcode: String) {
        val call = createOpenWeatherMapService().currentWeather(
            zipcode,
            "imperial",
            BuildConfig.OPEN_WEATHER_MAP_API_KEY
        )
        call.enqueue(object : Callback<CurrentWeather> {
            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e(
                    ForecastRespository::class.java.simpleName,
                    "error loading current weather",
                    t
                )
            }

            override fun onResponse(
                call: Call<CurrentWeather>, response: Response<CurrentWeather>
            ) {
                val weatherResponse = response.body()
                if (weatherResponse != null) {
                    _currentWeather.value = weatherResponse
                }
            }


        })
    }

    fun loadWeeklyForecast(zipcode: String) {
        val call = createOpenWeatherMapService().currentWeather(zipcode, "imperial", BuildConfig.OPEN_WEATHER_MAP_API_KEY)
        call.enqueue(object : Callback<CurrentWeather> {
            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e(ForecastRespository::class.java.simpleName, "error loading current weather for weekly forecast", t)
            }

            override fun onResponse(
                call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
                val weatherResponse = response.body()
                if (weatherResponse != null) {
                    //load 7 days forecast
                    val forecastCall = createOpenWeatherMapService().sevenDayForecast(
                        lat = weatherResponse.coord.lat,
                        lon = weatherResponse.coord.lon,
                        exclude = " current, minutely, hourly",
                        units = "imperial",
                        apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY
                    )
                    forecastCall.enqueue(object : Callback<WeeklyForecast> {
                        override fun onFailure(call: Call<WeeklyForecast>, t: Throwable) {
                            Log.e(
                                ForecastRespository::class.java.simpleName,
                                "error loading current weather",
                                t
                            )
                        }

                        override fun onResponse(
                            call: Call<WeeklyForecast>,
                            response: Response<WeeklyForecast>
                        ) {
                            val weeklyForecastResponse = response.body()
                            if (weeklyForecastResponse != null) {
                                _weeklyForecast.value = weeklyForecastResponse
                            }
                        }
                    })
                }
            }






            }
        )}
}


//    private fun getTempDescription(temp:Float): String{
//        return when (temp){
//            in Float.MIN_VALUE.rangeTo(0f) -> "Anything below 0 doesn't make sense"
//            in 0f.rangeTo(32f) -> "Way too cold"
//            in 32f.rangeTo(55f) -> "Colder than I would prefer"
//            in 55f.rangeTo(65f) -> "Getting Better"
//            in 65f.rangeTo(80f) -> "Sweet Spot"
//            in 80f.rangeTo(100f) -> "Way too hot"
//            in 100f.rangeTo(Float.MAX_VALUE) -> "I wish there A/C"
//            else  -> "Does not compute"
//        }
//    }
