package com.shashank.weatherapp.data.repositories

import com.shashank.weatherapp.data.local.WeatherDatabase
import com.shashank.weatherapp.data.model.WeatherDataResponse
import com.shashank.weatherapp.data.model.WeatherDetail
import com.shashank.weatherapp.data.network.ApiInterface
import com.shashank.weatherapp.data.network.SafeApiRequest

class WeatherRepository(
    private val api: ApiInterface,
    private val db: WeatherDatabase
) : SafeApiRequest() {

    suspend fun findCityWeather(cityName: String): WeatherDataResponse = apiRequest {
        api.findCityWeatherData(cityName)
    }

    suspend fun addWeather(weatherDetail: WeatherDetail) {
        db.getWeatherDao().addWeather(weatherDetail)
    }

    suspend fun fetchWeatherDetail(cityName: String): WeatherDetail? =
        db.getWeatherDao().fetchWeatherByCity(cityName)

    suspend fun fetchAllWeatherDetails(): List<WeatherDetail> =
        db.getWeatherDao().fetchAllWeatherDetails()
}
