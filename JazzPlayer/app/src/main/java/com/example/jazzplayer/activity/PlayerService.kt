package com.example.jazzplayer.activity

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.example.jazzplayer.DEFINES

class PlayerService : Service() {

    private val playList: ArrayList<MediaPlayer> = arrayListOf()

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        for (i in 0 until DEFINES.SONG_COUNT) {
            val mp = MediaPlayer.create(this, DEFINES.SONG_LIST[i])
            playList.add(mp)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "play" -> {
                playList[DEFINES.INDEX].start()
            }
            "pause" -> {
                playList[DEFINES.INDEX].pause()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        for (i in 0 until DEFINES.SONG_COUNT) {
            playList[i].release()
        }
        super.onDestroy()
    }
}