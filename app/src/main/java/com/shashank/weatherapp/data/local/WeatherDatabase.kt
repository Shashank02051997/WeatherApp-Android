package com.shashank.weatherapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shashank.weatherapp.data.local.dao.WeatherDetailDao
import com.shashank.weatherapp.data.model.WeatherDetail

@Database(
    entities = [WeatherDetail::class],
    version = 1
)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDetailDao

    companion object {
        const val DB_NAME = "weather_database"

        @Volatile
        private var INSTANCE: WeatherDatabase? = null

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                WeatherDatabase::class.java,
                DB_NAME
            ).build()
    }
}
