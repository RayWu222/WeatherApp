package com.example.weatherapp.forecast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.*
import com.example.weatherapp.api.DailyForecast
import com.example.weatherapp.api.WeeklyForecast

import com.example.weatherapp.details.ForecastDetailsActivityFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_current_forecast.*
import kotlinx.android.synthetic.main.fragment_weekly_forecast.*

/**
 * A simple [Fragment] subclass.
 */
class WeeklyForecastFragment : Fragment() {


    private val forecastRespository = ForecastRespository()
    private lateinit var locationRepository: LocationRepository
    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())

        val zipcode  = arguments?.getString(KEY_ZIPCODE) ?:""

        val view = inflater.inflate(R.layout.fragment_weekly_forecast, container, false)

        val emptyText = view.findViewById<TextView>(R.id.emptyText)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        val locationEntryButton : FloatingActionButton = view.findViewById(R.id.locationEntryButton)
        locationEntryButton.setOnClickListener{
            showLocationEntry()
        }


        val forecastList: RecyclerView = view.findViewById(R.id.forecastList)
        forecastList.layoutManager = LinearLayoutManager(requireContext())
        val dailyForecastAdapter = DailyForecastAdapter(tempDisplaySettingManager){forecast ->
            showForecastDetails(forecast)



        }
        forecastList.adapter = dailyForecastAdapter

        val weeklyForecastObserver = Observer<WeeklyForecast>{ weeklyForecast->
            emptyText.visibility = View.GONE
            progressBar.visibility = View.GONE
            //update our list adapter
            dailyForecastAdapter.submitList(weeklyForecast.daily)
        }
        forecastRespository.weeklyForecast.observe(viewLifecycleOwner, weeklyForecastObserver)

        locationRepository = LocationRepository(requireContext())
        val savedLocationObserver = Observer<Location>{savedLocation ->
            when(savedLocation){
                is Location.Zipcode -> {
                    progressBar.visibility = View.VISIBLE
                    forecastRespository.loadWeeklyForecast(savedLocation.zipcode)
                }
            }


        }

        locationRepository.savedLocation.observe(viewLifecycleOwner, savedLocationObserver)
        return view;
    }

    private fun showLocationEntry(){
        val action = WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToLocationEntryFragment()
        findNavController().navigate(action)
    }
    private fun showForecastDetails(forecast: DailyForecast){

        val temp = forecast.temp.max
        val description = forecast.weather[0].description
        val date = forecast.date
        val icon = forecast.weather[0].icon
        val action = WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToForecastDetailsActivityFragment(temp,description,date,icon)
        findNavController().navigate(action)
    }

    companion object{
        const val KEY_ZIPCODE = "key-zipcode"
        fun newInstance(zipcode:String):WeeklyForecastFragment{
            val fragment = WeeklyForecastFragment()

            val args = Bundle()

            args.putString(KEY_ZIPCODE, zipcode)
            fragment.arguments  = args

            return fragment
        }
    }
}
