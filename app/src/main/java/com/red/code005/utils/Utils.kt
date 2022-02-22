package com.red.code005.utils

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

fun loadBitmapFromUrl(context: Context, url: String?, onResourceReady: (Bitmap) -> Unit) {
    Glide.with(context).asBitmap()
        .load(url)
        .circleCrop()
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(
                resource: Bitmap,
                transition: Transition<in Bitmap>?
            ) {
                onResourceReady.invoke(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                // no use
            }
        })
}

fun Bitmap.vibrantPalette(context: Context, onPaletteReady: (Int, Int) -> Unit) {
    this.generatePalette { palette ->
        if (context.isSystemInDarkTheme()) {
            palette.darkVibrantSwatch?.let { paletteDark ->
                onPaletteReady.invoke(paletteDark.bodyTextColor, paletteDark.titleTextColor)
            }
        } else {
            palette.lightVibrantSwatch?.let { paletteLight ->
                onPaletteReady.invoke(paletteLight.bodyTextColor, paletteLight.titleTextColor)
            }
        }
    }
}

fun Context.isSystemInDarkTheme() =
    resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
