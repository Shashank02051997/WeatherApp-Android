package com.shashank.weatherapp.util

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.WindowManager
import com.shashank.weatherapp.R

class ProgressBar {
    private var dialog: Dialog? = null

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var gifProgress: ProgressBar? = null

        fun getInstance(): ProgressBar {
            if (gifProgress == null) {
                gifProgress = ProgressBar()
            }
            return gifProgress as ProgressBar
        }
    }

    fun showProgress(context: Context, cancelable: Boolean) {
        dialog = Dialog(context)

        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_progress_bar)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog?.window?.attributes)
        layoutParams.width = (context.resources.displayMetrics.widthPixels * 0.85).toInt()
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT

        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.setLayout(layoutParams.width, layoutParams.height)

        dialog?.setCancelable(cancelable)
        dialog?.setCanceledOnTouchOutside(cancelable)
        if (dialog?.isShowing == false) {
            dialog?.show()
        }
    }

    fun dismissProgress() {
        if (dialog != null && isDialogShowing()) {
            dialog?.dismiss()
            dialog = null
        }
    }

    fun isDialogShowing(): Boolean {
        if (dialog != null) {
            return dialog!!.isShowing
        }
        return false
    }
}
