package com.example.weatherapp.details

import android.os.Bundle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Float
import kotlin.String
import kotlin.jvm.JvmStatic

data class ForecastDetailsActivityFragmentArgs(
  val temp: Float,
  val description: String
) : NavArgs {
  fun toBundle(): Bundle {
    val result = Bundle()
    result.putFloat("temp", this.temp)
    result.putString("description", this.description)
    return result
  }

  companion object {
    @JvmStatic
    fun fromBundle(bundle: Bundle): ForecastDetailsActivityFragmentArgs {
      bundle.setClassLoader(ForecastDetailsActivityFragmentArgs::class.java.classLoader)
      val __temp : Float
      if (bundle.containsKey("temp")) {
        __temp = bundle.getFloat("temp")
      } else {
        throw IllegalArgumentException("Required argument \"temp\" is missing and does not have an android:defaultValue")
      }
      val __description : String?
      if (bundle.containsKey("description")) {
        __description = bundle.getString("description")
        if (__description == null) {
          throw IllegalArgumentException("Argument \"description\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"description\" is missing and does not have an android:defaultValue")
      }
      return ForecastDetailsActivityFragmentArgs(__temp, __description)
    }
  }
}
