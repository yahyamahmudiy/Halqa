package com.example.halqa.extension

import android.widget.ImageView
import android.widget.TextView
import android.text.method.ScrollingMovementMethod


fun String.firstCap() = if (this.isNotBlank()) this.substring(0, 1).uppercase() + this.substring(
    1,
    this.length
) else this

fun ImageView.setImage(drawable: Int) = this.setImageResource(drawable)

fun TextView.makeVerticallyScrollable() =
    ScrollingMovementMethod().also { this.movementMethod = it }
