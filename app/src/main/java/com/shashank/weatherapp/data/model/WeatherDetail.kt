package com.shashank.weatherapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shashank.weatherapp.data.model.WeatherDetail.Companion.TABLE_NAME

/**
 * Data class for Database entity and Serialization.
 */
@Entity(tableName = TABLE_NAME)
data class WeatherDetail(

    @PrimaryKey
    var id: Int? = 0,
    var temp: Double? = null,
    var icon: String? = null,
    var cityName: String? = null,
    var countryName: String? = null,
    var dateTime: String? = null
) {
    companion object {
        const val TABLE_NAME = "weather_detail"
    }
}