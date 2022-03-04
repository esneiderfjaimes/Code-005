package com.red.code005.utils

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition

fun loadBitmapFromUrl(context: Context, url: String?, onResourceReady: (Bitmap) -> Unit) {
    Glide.with(context).asBitmap()
        .load(url)
        .circleCrop()
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(
                resource: Bitmap,
                transition: Transition<in Bitmap>?,
            ) {
                onResourceReady.invoke(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) = Unit
        })
}

fun Uri.loadDrawable(
    context: Context,
    onReady: (Drawable) -> Unit = {},
    onError: (GlideException?) -> Unit = {},
) {
    Glide.with(context).asDrawable()
        .load(this)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?, model: Any?,
                target: Target<Drawable>?, isFirstResource: Boolean,
            ): Boolean {
                onError.invoke(e)
                return false
            }

            override fun onResourceReady(
                r: Drawable?, m: Any?, t: Target<Drawable>?,
                d: DataSource?, b: Boolean,
            ) = false
        })
        .into(object : CustomTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, t: Transition<in Drawable>?) {
                onReady.invoke(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) = Unit
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
