package com.shashank.weatherapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.shashank.weatherapp.R
import com.shashank.weatherapp.data.model.WeatherDetail
import com.shashank.weatherapp.databinding.ListItemSearchedCityTemperatureBinding
import com.shashank.weatherapp.util.AppConstants
import com.shashank.weatherapp.util.AppUtils

class CustomAdapterSearchedCityTemperature :
    RecyclerView.Adapter<CustomAdapterSearchedCityTemperature.ViewHolder>() {

    private val weatherDetailList = ArrayList<WeatherDetail>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ListItemSearchedCityTemperatureBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_searched_city_temperature,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(weatherDetailList[position])
    }

    override fun getItemCount(): Int = weatherDetailList.size

    fun setData(
        newWeatherDetail: List<WeatherDetail>
    ) {
        weatherDetailList.clear()
        weatherDetailList.addAll(newWeatherDetail)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ListItemSearchedCityTemperatureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindItems(weatherDetail: WeatherDetail) {
            binding.apply {
                val iconCode = weatherDetail.icon?.replace("n", "d")
                AppUtils.setGlideImage(
                    imageWeatherSymbol,
                    AppConstants.WEATHER_API_IMAGE_ENDPOINT + "${iconCode}@4x.png"
                )
                textCityName.text =
                    "${weatherDetail.cityName?.capitalize()}, ${weatherDetail.countryName}"
                textTemperature.text = weatherDetail.temp.toString()
                textDateTime.text = weatherDetail.dateTime
            }
        }
    }
}
