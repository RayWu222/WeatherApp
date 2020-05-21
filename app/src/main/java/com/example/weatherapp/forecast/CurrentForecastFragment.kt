package com.example.weatherapp.forecast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.*

import com.example.weatherapp.details.ForecastDetailsActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A simple [Fragment] subclass.
 */
class CurrentForecastFragment : Fragment() {

    private  lateinit var appNavigator: AppNavigator

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appNavigator = context as AppNavigator
    }
    private val forecastRespository = ForecastRespository()

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())

        val zipcode  = arguments!!.getString(KEY_ZIPCODE) ?:""
        // Inflate the layout fo
        // r this fragment
        val view = inflater.inflate(R.layout.fragment_current_forecast, container, false)

        val locationEntryButton : FloatingActionButton = view.findViewById(R.id.locationEntryButton)
        locationEntryButton.setOnClickListener{
            appNavigator.navigateToLocationEntry()
        }


        val forecastList: RecyclerView = view.findViewById(R.id.forecastList)
        forecastList.layoutManager = LinearLayoutManager(requireContext())
        val dailyForecastAdapter = DailyForecastAdapter(tempDisplaySettingManager){forecast ->
            showForecastDetails(forecast)



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

    private fun showForecastDetails(forecast: DailyForecast){
        val forecastDetailsIntent = Intent(requireContext(), ForecastDetailsActivity:: class.java)
        forecastDetailsIntent.putExtra("key_temp", forecast.temp )
        forecastDetailsIntent.putExtra("key_description", forecast.description)
        startActivity(forecastDetailsIntent)
    }

    companion object{
        const val KEY_ZIPCODE = "key-zipcode"
        fun newInstance(zipcode:String):CurrentForecastFragment{
            val fragment = CurrentForecastFragment()

            val args = Bundle()

            args.putString(KEY_ZIPCODE, zipcode)
            fragment.arguments  = args

            return fragment
        }
    }
}
