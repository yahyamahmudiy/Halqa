package com.example.halqa.helper

import android.content.Context
import androidx.core.content.edit

class SharePref(context: Context) {
    private val pref = context.getSharedPreferences("first", Context.MODE_PRIVATE)

    var isSaved: Boolean
        get() = pref.getBoolean("isSaved", true)
        set(value) = pref.edit { this.putBoolean("isSaved", value) }
}
