package com.shashank.weatherapp.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.shashank.weatherapp.R
import com.shashank.weatherapp.databinding.ActivityWeatherBinding
import com.shashank.weatherapp.ui.adapters.CustomAdapterSearchedCityTemperature
import com.shashank.weatherapp.ui.viewmodel.WeatherViewModel
import com.shashank.weatherapp.ui.viewmodelfactory.WeatherViewModelFactory
import com.shashank.weatherapp.util.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class WeatherActivity : AppCompatActivity(), KodeinAware {

    override val kodein by closestKodein()
    private lateinit var dataBind: ActivityWeatherBinding
    private val factory: WeatherViewModelFactory by instance()
    private val viewModel: WeatherViewModel by lazy {
        ViewModelProvider(this, factory).get(WeatherViewModel::class.java)
    }
    private lateinit var customAdapterSearchedCityTemperature: CustomAdapterSearchedCityTemperature

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBind = DataBindingUtil.setContentView(this, R.layout.activity_weather)
        setupUI()
        observeAPICall()
    }

    private fun setupUI() {
        initializeRecyclerView()
        dataBind.inputFindCityWeather.setOnEditorActionListener { view, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.fetchWeatherDetailFromDb((view as EditText).text.toString())
                viewModel.fetchAllWeatherDetailsFromDb()
            }
            false
        }
    }

    private fun initializeRecyclerView() {
        customAdapterSearchedCityTemperature = CustomAdapterSearchedCityTemperature()
        val mLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        dataBind.recyclerViewSearchedCityTemperature.apply {
            layoutManager = mLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = customAdapterSearchedCityTemperature
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeAPICall() {
        viewModel.weatherLiveData.observe(this, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                }
                is State.Success -> {
                    dataBind.textLabelSearchForCity.hide()
                    dataBind.imageCity.hide()
                    dataBind.constraintLayoutShowingTemp.show()
                    dataBind.inputFindCityWeather.text?.clear()
                    state.data.let { weatherDetail ->
                        val iconCode = weatherDetail.icon?.replace("n", "d")
                        AppUtils.setGlideImage(
                            dataBind.imageWeatherSymbol,
                            AppConstants.WEATHER_API_IMAGE_ENDPOINT + "${iconCode}@4x.png"
                        )
                        changeBgAccToTemp(iconCode)
                        dataBind.textTodaysDate.text =
                            AppUtils.getCurrentDateTime(AppConstants.DATE_FORMAT)
                        dataBind.textTemperature.text = weatherDetail.temp.toString()
                        dataBind.textCityName.text =
                            "${weatherDetail.cityName?.capitalize()}, ${weatherDetail.countryName}"
                    }

                }
                is State.Error -> {
                    showToast(state.message)
                }
            }
        })

        viewModel.weatherDetailListLiveData.observe(this, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                }
                is State.Success -> {
                    if (state.data.isEmpty()) {
                        dataBind.recyclerViewSearchedCityTemperature.hide()
                    } else {
                        dataBind.recyclerViewSearchedCityTemperature.show()
                        customAdapterSearchedCityTemperature.setData(state.data)
                    }
                }
                is State.Error -> {
                    showToast(state.message)
                }
            }
        })
    }

    private fun changeBgAccToTemp(iconCode: String?) {
        when (iconCode) {
            "01d", "02d", "03d" -> dataBind.imageWeatherHumanReaction.setImageResource(R.drawable.sunny_day)
            "04d", "09d", "10d", "11d" -> dataBind.imageWeatherHumanReaction.setImageResource(R.drawable.raining)
            "13d", "50d" -> dataBind.imageWeatherHumanReaction.setImageResource(R.drawable.snowfalling)
        }
    }

}