package com.example.weatherapp.forecast

import android.os.Bundle
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.weatherapp.R
import kotlin.Float
import kotlin.Int
import kotlin.Long
import kotlin.String

class WeeklyForecastFragmentDirections private constructor() {
  private data class ActionWeeklyForecastFragmentToForecastDetailsActivityFragment(
    val temp: Float,
    val description: String,
    val date: Long,
    val icon: String
  ) : NavDirections {
    override fun getActionId(): Int =
        R.id.action_weeklyForecastFragment_to_forecastDetailsActivityFragment

    override fun getArguments(): Bundle {
      val result = Bundle()
      result.putFloat("temp", this.temp)
      result.putString("description", this.description)
      result.putLong("date", this.date)
      result.putString("icon", this.icon)
      return result
    }
  }

  companion object {
    fun actionWeeklyForecastFragmentToForecastDetailsActivityFragment(
      temp: Float,
      description: String,
      date: Long,
      icon: String
    ): NavDirections = ActionWeeklyForecastFragmentToForecastDetailsActivityFragment(temp,
        description, date, icon)

    fun actionWeeklyForecastFragmentToLocationEntryFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_weeklyForecastFragment_to_locationEntryFragment)
  }
}
