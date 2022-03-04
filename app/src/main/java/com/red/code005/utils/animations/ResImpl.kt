package com.red.code005.utils.animations

import android.R.attr.state_enabled
import android.content.res.ColorStateList
import android.graphics.Color

const val DEFAULT_COLOR = Color.RED

fun ColorStateList?.tint(): Int =
    this?.getColorForState(arrayOf(state_enabled).toIntArray(), DEFAULT_COLOR) ?: DEFAULT_COLOR