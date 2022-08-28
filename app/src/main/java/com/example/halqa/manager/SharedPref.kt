package com.example.halqa.manager

import android.content.Context
import androidx.core.content.edit
import com.example.halqa.utils.Constants.NOTSAVED

class SharedPref(context: Context) {
    private val pref = context.getSharedPreferences("halqa", Context.MODE_PRIVATE)

    fun saveString(key: String, value: String?) {
        val editor = pref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    var isSaved: Boolean
        get() = pref.getBoolean("isSaved", true)
        set(value) = pref.edit { this.putBoolean("isSaved", value) }

    var isSavedAudioHalqa: String?
        get() = pref.getString("isSaveHalqa", NOTSAVED)
        set(value) = pref.edit { this.putString("isSaveHalqa", value) }

    var isSavedAudioJangchi: String?
        get() = pref.getString("isSaveJangchi", NOTSAVED)
        set(value) = pref.edit { this.putString("isSaveJangchi", value) }

    var isOneCreate: Boolean
        get() {
            return pref.getBoolean("KEY2", true)
        }
        set(value) {
            pref.edit().putBoolean("KEY2", value).apply()
        }

    fun getString(key: String): String? {
        return pref.getString(key, "")
    }

    fun saveBoolean(key: String, value: Boolean) {
        val editor = pref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String): Boolean {
        return pref.getBoolean(key, false)
    }

    fun saveInt(key: String, value: Int) {
        val editor = pref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String): Int {
        return pref.getInt(key, 12)
    }

    fun saveFloat(key: String, value: Float) {
        val editor = pref.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    fun getFloat(key: String): Float {
        return pref.getFloat(key, 0F)
    }

}