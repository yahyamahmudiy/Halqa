package com.example.halqa.mediaplayer

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class AudioController @Inject constructor(@ApplicationContext val context: Context) {

    private var mediaPlayer: MediaPlayer = MediaPlayer()


    fun playMediaPlayer() {
        mediaPlayer.start()
    }

    fun pauseMediaPlayer() {
        mediaPlayer.pause()
    }


    fun playSource(filePath: String) =
        try {
            Log.d("TAG", "playSource: $filePath")
            resetPlayer()
            mediaPlayer.setDataSource(context, File(filePath).toUri())
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {

        }



    private fun resetPlayer() =
        try {
            mediaPlayer.stop()
            mediaPlayer.reset()
        } catch (e: Exception) {
            e.printStackTrace()
        }


    fun forward15Seconds() {
        if (mediaPlayer.currentPosition + 15000 <= mediaPlayer.duration)
            mediaPlayer.seekTo(mediaPlayer.currentPosition + 15000)
        else mediaPlayer.seekTo(mediaPlayer.duration)
    }


    fun backward15Seconds() {
        if (mediaPlayer.currentPosition >= 15000)
            mediaPlayer.seekTo(mediaPlayer.currentPosition - 15000)
        else mediaPlayer.seekTo(0)
    }


    fun isPlaying(): Boolean = mediaPlayer.isPlaying

    fun duration(): Int = mediaPlayer.duration

    fun currentPosition(): Int = mediaPlayer.currentPosition

    fun seekTo(p1: Int) = mediaPlayer.seekTo(p1)
}