package com.red.code005.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingAdapters {

    @BindingAdapter("photoUrl")
    @JvmStatic
    fun loadImage(view: ImageView, url: String?) {
        if (url.isNullOrBlank()) return
        Glide.with(view.context).load(url).circleCrop().into(view)
    }
}
