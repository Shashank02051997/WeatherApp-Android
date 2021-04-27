package com.shashank.weatherapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

object AppUtils {

    fun showProgressBar(requireContext: Context) {
        if (!ProgressBar.getInstance().isDialogShowing()) {
            ProgressBar.getInstance().showProgress(requireContext, false)
        }
    }

    fun hideProgressBar() {
        ProgressBar.getInstance().dismissProgress()
    }

    fun setGlideImage(image: ImageView, url: String) {

        Glide.with(image).load(url)
            .thumbnail(0.5f)
            .into(image)

    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateTime(dateFormat: String): String =
        SimpleDateFormat(dateFormat).format(Date())

    @SuppressLint("SimpleDateFormat")
    fun isTimeExpired(dateTimeSavedWeather: String?): Boolean {
        dateTimeSavedWeather?.let {
            val currentDateTime = Date()
            val savedWeatherDateTime =
                SimpleDateFormat(AppConstants.DATE_FORMAT_1).parse(it)
            val diff: Long = currentDateTime.time - savedWeatherDateTime.time
            val seconds = diff / 1000
            val minutes = seconds / 60
            if (minutes > 10)
                return true
        }
        return false
    }
}