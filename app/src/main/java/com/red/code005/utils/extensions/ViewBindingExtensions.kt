package com.red.code005.utils.extensions

import android.view.View
import com.red.code005.databinding.NavigationRailFabBinding

fun View.inRailFAB(body: NavigationRailFabBinding.() -> Unit = {}) =
    NavigationRailFabBinding.bind(this).apply(body)
