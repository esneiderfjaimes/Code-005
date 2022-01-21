package com.red.code005

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel

fun AndroidViewModel.toast(text: String) {
    Toast.makeText(getApplication(), text, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(text: String) {
    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(@StringRes text: Int) {
    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
}