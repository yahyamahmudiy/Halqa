package com.example.halqa.mediaplayer

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class AudioController @Inject constructor(@ApplicationContext val context: Context) :
    MediaPlayer() {


    fun playMediaPlayer() {
        this.start()
    }

    fun pauseMediaPlayer() {
        this.pause()
    }

    fun playSource(filePath: String) =
        try {
            resetPlayer()
            this.setDataSource(context, File(filePath).toUri())
            this.setAudioStreamType(AudioManager.STREAM_MUSIC)
            this.prepare()
            this.start()
        } catch (e: Exception) {

        }

    private fun resetPlayer() =
        try {
            this.stop()
            this.reset()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    fun forward15Seconds() {
        if (this.currentPosition + 15000 <= this.duration)
            this.seekTo(this.currentPosition + 15000)
        else this.seekTo(this.duration)
    }


    fun backward15Seconds() {
        if (this.currentPosition >= 15000)
            this.seekTo(this.currentPosition - 15000)
        else this.seekTo(0)
    }
}