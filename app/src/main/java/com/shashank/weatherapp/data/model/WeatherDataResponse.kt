package com.shashank.weatherapp.data.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class WeatherDataResponse(
    @SerializedName("base")
    val base: String,
    @SerializedName("clouds")
    val clouds: Clouds,
    @SerializedName("cod")
    val cod: Int,
    @SerializedName("coord")
    val coord: Coord,
    @SerializedName("dt")
    val dt: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("main")
    val main: Main,
    @SerializedName("name")
    val name: String,
    @SerializedName("sys")
    val sys: Sys,
    @SerializedName("timezone")
    val timezone: Int,
    @SerializedName("visibility")
    val visibility: Int,
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("wind")
    val wind: Wind
) {
    @Keep
    data class Clouds(
        @SerializedName("all")
        val all: Int
    )

    @Keep
    data class Coord(
        @SerializedName("lat")
        val lat: Double,
        @SerializedName("lon")
        val lon: Double
    )

    @Keep
    data class Main(
        @SerializedName("feels_like")
        val feelsLike: Double,
        @SerializedName("grnd_level")
        val grndLevel: Int,
        @SerializedName("humidity")
        val humidity: Int,
        @SerializedName("pressure")
        val pressure: Int,
        @SerializedName("sea_level")
        val seaLevel: Int,
        @SerializedName("temp")
        val temp: Double,
        @SerializedName("temp_max")
        val tempMax: Double,
        @SerializedName("temp_min")
        val tempMin: Double
    )

    @Keep
    data class Sys(
        @SerializedName("country")
        val country: String,
        @SerializedName("sunrise")
        val sunrise: Int,
        @SerializedName("sunset")
        val sunset: Int
    )

    @Keep
    data class Weather(
        @SerializedName("description")
        val description: String,
        @SerializedName("icon")
        val icon: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("main")
        val main: String
    )

    @Keep
    data class Wind(
        @SerializedName("deg")
        val deg: Int,
        @SerializedName("gust")
        val gust: Double,
        @SerializedName("speed")
        val speed: Double
    )
}