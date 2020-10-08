package com.example.weatherapp.details

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.example.weatherapp.*
import com.example.weatherapp.databinding.FragmentForecastDetailsBinding
import java.text.SimpleDateFormat
import java.util.*


class ForecastDetailsActivityFragment : Fragment() {

    private val args: ForecastDetailsActivityFragmentArgs by navArgs()

    private lateinit var viewModelFactory: ForecastDetailsViewModelFactory


    private val viewModel: ForecastDetailsViewModel by viewModels(
        factoryProducer = {viewModelFactory}
    )

    private var _binding: FragmentForecastDetailsBinding? = null
    //This property only valid between onCreateView and onDestroyView
    private val binding get()= _binding!!

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForecastDetailsBinding.inflate(inflater, container, false)
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelFactory = ForecastDetailsViewModelFactory(args)
        val viewStateObserver = Observer<ForecastDetailsViewState>{ viewState ->

            //update the UI
            binding.tempText.text = formatTempForDisplay(viewState.temp, tempDisplaySettingManager.getTempDisplaySetting())
            binding.descriptionText.text = viewState.description
            binding.dateText.text = viewState.date
            binding.forecastIcon.load(viewState.iconUrl)



        }
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    }
