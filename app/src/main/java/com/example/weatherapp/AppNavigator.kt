package com.example.weatherapp

interface AppNavigator {

    fun navigateToCurrentForecast(zipcode:String)
    fun navigateToLocationEntry()
}