package com.red.code005.utils.animations

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.view.animation.AccelerateInterpolator
import androidx.annotation.ColorInt

const val DEFAULT_DURATION: Long = 500

fun animOfFloat(from: Float, to: Float, update: (Float) -> Unit = {}): ValueAnimator =
    ValueAnimator.ofFloat(from, to).apply {
        duration = DEFAULT_DURATION
        addUpdateListener { animator ->
            update.invoke(animator.animatedValue as Float)
        }
    }

fun colorAnim(@ColorInt from: Int?, @ColorInt to: Int?, update: (Int) -> Unit = {}): ValueAnimator =
    ValueAnimator.ofObject(ArgbEvaluator(), from, to).apply {
        duration = DEFAULT_DURATION
        interpolator = AccelerateInterpolator()
        addUpdateListener { animator ->
            update.invoke(animator.animatedValue as Int)
        }
    }
