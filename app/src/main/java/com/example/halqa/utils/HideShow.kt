package com.example.halqa.utils

import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation

fun View.hide() = try {
    this.visibility = View.GONE
} catch (e: Exception) {

}

fun View.invisible() = try {
    this.visibility = View.INVISIBLE
} catch (e: Exception) {

}

fun View.show() = try {
    this.visibility = View.VISIBLE
} catch (e: Exception) {

}

fun View.slideUp() {
    this.show()
    val animate = TranslateAnimation(
        0f,  // fromXDelta
        0f,  // toXDelta
        this.height.toFloat(),  // fromYDelta
        0f
    ) // toYDelta
    animate.duration = 1
    animate.fillAfter = true
    this.startAnimation(animate)
}

// slide the view from its current position to below itself
fun View.slideDown() {
    val animate = TranslateAnimation(
        0f,  // fromXDelta
        0f,  // toXDelta
        0f,  // fromYDelta
        this.height.toFloat()
    ) // toYDelta
    animate.duration = 500
    animate.fillAfter = true
    this.startAnimation(animate)
}