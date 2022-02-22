package com.red.code005.utils

import android.view.View
import androidx.annotation.StringRes
import com.red.code005.databinding.CommonQueryBinding

fun CommonQueryBinding.loading() {
    parent.visibility = View.VISIBLE
    progress.visibility = View.VISIBLE
    message.visibility = View.GONE
}

fun CommonQueryBinding.message(msg: String) {
    parent.visibility = View.VISIBLE
    progress.visibility = View.GONE
    message.visibility = View.VISIBLE
    message.text = msg
}

fun CommonQueryBinding.message(@StringRes id: Int) {
    parent.visibility = View.VISIBLE
    progress.visibility = View.GONE
    message.visibility = View.VISIBLE
    message.setText(id)
}

fun CommonQueryBinding.error(exception: Throwable) {
    message("An error has occurred\n\n${exception.message}")
}

fun CommonQueryBinding.hide() {
    parent.visibility = View.GONE
}