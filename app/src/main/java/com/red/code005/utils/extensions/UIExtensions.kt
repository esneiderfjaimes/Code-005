package com.red.code005.utils.extensions

import android.animation.ValueAnimator
import android.graphics.drawable.Animatable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.google.android.material.shape.RelativeCornerSize
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.Shapeable

// Animations

fun ImageView.startAnimationDrawable(@DrawableRes resId: Int) {
    setImageResource(resId)
    (drawable as Animatable).start()
}

fun Shapeable.animShape(from: Float, to: Float, reverse: Boolean = false) {
    ValueAnimator.ofFloat(from, to
/*        if (!reverse) from else to,
        if (!reverse) from else to*/
    ).apply {
        duration = 500
        if (!reverse) start()
        else reverse()
    }.apply {
        addUpdateListener { updatedAnimation ->
            shapeAppearanceModel = ShapeAppearanceModel.Builder(shapeAppearanceModel).apply {
                setAllCornerSizes(RelativeCornerSize(updatedAnimation.animatedValue as Float))
            }.build()
        }
    }
}

fun Shapeable.animShape(from: Float, to: Float): ValueAnimator = ValueAnimator.ofFloat(from, to).apply {
    duration = 500
    addUpdateListener { updatedAnimation ->
        shapeAppearanceModel = ShapeAppearanceModel.Builder(shapeAppearanceModel).apply {
            setAllCornerSizes(RelativeCornerSize(updatedAnimation.animatedValue as Float))
        }.build()
    }
}