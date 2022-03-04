package com.red.code005.utils.animations

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.drawable.Animatable2
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import com.google.android.material.shape.RelativeCornerSize
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.Shapeable

// region Drawable Animations

fun ImageView.drawableAnim(@DrawableRes resId: Int, onEnd: () -> Unit = {}) {
    var animate: Animatable2? = if (drawable is Animatable2) drawable as Animatable2 else null
    if (animate?.isRunning == true) animate.stop()
    setImageResource(resId)
    animate = if (drawable is Animatable2) drawable as Animatable2 else return
    animate.registerAnimationCallback(object : Animatable2.AnimationCallback() {
        override fun onAnimationEnd(drawable: Drawable?) {
            super.onAnimationEnd(drawable)
            onEnd.invoke()
        }
    })
    animate.start()
}

fun ImageView.drawableAnim(@DrawableRes resId: Int, @DrawableRes resIdWhenEnd: Int) {
    drawableAnim(resId) { setImageResource(resIdWhenEnd) }
}

// endregion
// region Corner Animations

/**
 * Corner animation of a Shapeable component
 *
 * @param to final relative percentage that the corner of the component will have.
 * @return A ValueAnimator object that is set to animate the corners of the component
 * or null if it does not correspond to what is required.
 */
fun Shapeable.cornerAnim(@FloatRange(from = 0.0, to = 1.0) to: Float): ValueAnimator? =
    if (shapeAppearanceModel.topRightCornerSize is RelativeCornerSize) {
        animOfFloat(
            (shapeAppearanceModel.topRightCornerSize as RelativeCornerSize).relativePercent, to) {
            shapeAppearanceModel = ShapeAppearanceModel.Builder(shapeAppearanceModel).apply {
                setAllCornerSizes(RelativeCornerSize(it))
            }.build()
        }
    } else null

// endregion
// region Color Animations

/**
 * Background color animation
 */
fun View.bgColorAnim(@ColorInt to: Int): ValueAnimator =
    colorAnim(from = backgroundTintList.tint(), to = to) {
        backgroundTintList = ColorStateList.valueOf(it)
    }

/**
 * Image color animation
 */
fun ImageView.imgColorAnim(@ColorInt to: Int): ValueAnimator =
    colorAnim(from = imageTintList.tint(), to = to) {
        imageTintList = ColorStateList.valueOf(it)
    }

// endregion
