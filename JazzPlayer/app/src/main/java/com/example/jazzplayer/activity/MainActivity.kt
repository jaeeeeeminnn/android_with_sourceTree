package com.example.jazzplayer.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jazzplayer.DEFINES
import com.example.jazzplayer.R
import com.example.jazzplayer.adapter.PlayerAdapter
import com.example.jazzplayer.model.PlayerModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAdapter: PlayerAdapter

    val playerList: ArrayList<PlayerModel> = arrayListOf()

    private val CHANNEL_ID: String = "JP"
    private val CHANNEL_NAME: String = "Jazz_Player"
    private var title: String = ""
    private var NOTIFICATION_ID: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadJazz()

        /**
         * if (하나라도 position이 눌려있었다면) {
         *      if (근데 그 position이 현재 postion과 일치하면) {
         *          현재 poistion을 pause하고 false로
         *      } else {
         *          이전 position을 stop하고 false로
         *          현재 position을 play하고 true로
         *      }
         * } else {
         *      아무것도 재생하지 않으므로 현재 position거 play하고 true로
         * }
         * item click 시 notification도 나타나야 함.
         */
        var isPlaying : ArrayList<Boolean> = arrayListOf()
        for (i in 0 until DEFINES.SONG_COUNT) {
            isPlaying.add(false)
        }
        mAdapter.setItemClickListener(object : PlayerAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                if (isPlaying[position] == true) {
                    val intent = Intent(this@MainActivity, PlayerService::class.java)
                    intent.action = "pause"
                    title = playerList[position].title.toString()
                    isPlaying[position] = false
                    startService(intent)
                    sendNotification(isPause = true)
                } else {
                    val intent = Intent(this@MainActivity, PlayerService::class.java)
                    intent.action = "play"
                    title = playerList[position].title.toString()
                    isPlaying[position] = true
                    startService(intent)
                    sendNotification(isPause = false)
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for Jazz Player"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(isPause: Boolean) {
        var text = ""
        if (isPause) {
            text = "is now paused"
        } else {
            text = "is now playing"
        }


        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
        createNotificationChannel(notificationManager)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_baseline_library_music_24)
            setContentTitle(title)
            setContentText(text)
            setAutoCancel(true)
            setPriority(NotificationCompat.PRIORITY_DEFAULT)
            setContentIntent(pendingIntent)
        }
        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, notification.build())
        }
    }

    private fun loadJazz() {
        mAdapter = PlayerAdapter()

        playerList.add(
            PlayerModel(
                R.drawable.img_a_string_of_pearls,
                "A String Of Pearls",
                "BBC Big Band",
                "Glenn Miller"
            )
        )
        playerList.add(PlayerModel(R.drawable.img_caravan, "Caravan", "Variet", "Urvin Miles"))
        playerList.add(
            PlayerModel(
                R.drawable.img_it_dont_mean_a_thing,
                "It don't mean a Thing",
                "If it Ain't Got That Swing",
                "Duke Elington"
            )
        )
        playerList.add(
            PlayerModel(
                R.drawable.img_round_midnight,
                "Round Midnight",
                "Monk",
                "Cutie Williams & Sellonius Monk"
            )
        )
        playerList.add(
            PlayerModel(
                R.drawable.img_salt_peanuts,
                "Salt Peanuts",
                "Salt Peanuts",
                "Dizzy Gillespie"
            )
        )
        playerList.add(
            PlayerModel(
                R.drawable.img_the_chicken,
                "The Chicken",
                "Stuttgart Aria",
                "Jaco Pastorius"
            )
        )
        playerList.add(
            PlayerModel(
                R.drawable.img_what_a_wonderful_world,
                "What a Wonderful World",
                "Louis VI",
                "Louis Armstrong"
            )
        )

        mAdapter.setList(playerList)
        main_list.layoutManager = LinearLayoutManager(this)
        main_list.adapter = mAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}