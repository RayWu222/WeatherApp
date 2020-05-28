package com.example.weatherapp.forecast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.*

import com.example.weatherapp.details.ForecastDetailsActivityFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A simple [Fragment] subclass.
 */
class WeeklyForecastFragment : Fragment() {


    private val forecastRespository = ForecastRespository()

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())

        val zipcode  = arguments?.getString(KEY_ZIPCODE) ?:""
        // Inflate the layout fo
        // r this fragment
        val view = inflater.inflate(R.layout.fragment_weekly_forecast, container, false)

        val locationEntryButton : FloatingActionButton = view.findViewById(R.id.locationEntryButton)
        locationEntryButton.setOnClickListener{
            showLocationEntry()
        }


        val forecastList: RecyclerView = view.findViewById(R.id.forecastList)
        forecastList.layoutManager = LinearLayoutManager(requireContext())
        val dailyForecastAdapter = DailyForecastAdapter(tempDisplaySettingManager){forecast ->
            showLocationEntry()



        }
        forecastList.adapter = dailyForecastAdapter

        val weeklyForecastObserver = Observer<List<DailyForecast>>{ forecastItems ->
            //update our list adapter
            dailyForecastAdapter.submitList(forecastItems)
        }
        forecastRespository.weeklyForecast.observe(this, weeklyForecastObserver)
        forecastRespository.loadForecast(zipcode)
        return view;
    }

    private fun showLocationEntry(){
        val action = WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToLocationEntryFragment()
        findNavController().navigate(action)
    }
    private fun showForecastDetails(forecast: DailyForecast){
        val action = WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToForecastDetailsActivityFragment(forecast.temp,forecast.description)
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
