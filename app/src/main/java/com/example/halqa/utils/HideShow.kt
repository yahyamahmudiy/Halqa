package com.example.halqa.utils

import android.view.View

fun View.hide() = try {
    this.visibility = View.GONE
} catch (e: Exception) {

}

fun View.show() = try {
    this.visibility = View.VISIBLE
} catch (e: Exception) {

}