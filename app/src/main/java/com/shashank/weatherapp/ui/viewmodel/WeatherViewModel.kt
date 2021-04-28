package com.shashank.weatherapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shashank.weatherapp.data.model.WeatherDataResponse
import com.shashank.weatherapp.data.model.WeatherDetail
import com.shashank.weatherapp.data.repositories.WeatherRepository
import com.shashank.weatherapp.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel(private val repository: WeatherRepository) :
    ViewModel() {

    private val _weatherLiveData =
        MutableLiveData<Event<State<WeatherDetail>>>()
    val weatherLiveData: LiveData<Event<State<WeatherDetail>>>
        get() = _weatherLiveData

    private val _weatherDetailListLiveData =
        MutableLiveData<Event<State<List<WeatherDetail>>>>()
    val weatherDetailListLiveData: LiveData<Event<State<List<WeatherDetail>>>>
        get() = _weatherDetailListLiveData

    private lateinit var weatherResponse: WeatherDataResponse

    private fun findCityWeather(cityName: String) {
        _weatherLiveData.postValue(Event(State.loading()))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                weatherResponse =
                    repository.findCityWeather(cityName)
                addWeatherDetailIntoDb(weatherResponse)
                withContext(Dispatchers.Main) {
                    val weatherDetail = WeatherDetail()
                    weatherDetail.icon = weatherResponse.weather.first().icon
                    weatherDetail.cityName = weatherResponse.name
                    weatherDetail.countryName = weatherResponse.sys.country
                    weatherDetail.temp = weatherResponse.main.temp
                    _weatherLiveData.postValue(
                        Event(
                            State.success(
                                weatherDetail
                            )
                        )
                    )
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _weatherLiveData.postValue(Event(State.error(e.message ?: "")))
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _weatherLiveData.postValue(Event(State.error(e.message ?: "")))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _weatherLiveData.postValue(
                        Event(
                            State.error(
                                e.message ?: ""
                            )
                        )
                    )
                }
            }
        }
    }

    private suspend fun addWeatherDetailIntoDb(weatherResponse: WeatherDataResponse) {
        val weatherDetail = WeatherDetail()
        weatherDetail.id = weatherResponse.id
        weatherDetail.icon = weatherResponse.weather.first().icon
        weatherDetail.cityName = weatherResponse.name.toLowerCase()
        weatherDetail.countryName = weatherResponse.sys.country
        weatherDetail.temp = weatherResponse.main.temp
        weatherDetail.dateTime = AppUtils.getCurrentDateTime(AppConstants.DATE_FORMAT_1)
        repository.addWeather(weatherDetail)
    }

    fun fetchWeatherDetailFromDb(cityName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherDetail = repository.fetchWeatherDetail(cityName.toLowerCase())
            withContext(Dispatchers.Main) {
                if (weatherDetail != null) {
                    // Return true of current date and time is greater then the saved date and time of weather searched
                    if (AppUtils.isTimeExpired(weatherDetail.dateTime)) {
                        findCityWeather(cityName)
                    } else {
                        _weatherLiveData.postValue(
                            Event(
                                State.success(
                                    weatherDetail
                                )
                            )
                        )
                    }

                } else {
                    findCityWeather(cityName)
                }

            }
        }
    }

    fun fetchAllWeatherDetailsFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherDetailList = repository.fetchAllWeatherDetails()
            withContext(Dispatchers.Main) {
                _weatherDetailListLiveData.postValue(
                    Event(
                        State.success(weatherDetailList)
                    )
                )
            }
        }
    }
}
