package com.example.restpoints.activity

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.restpoints.DEFINES
import com.example.restpoints.HTTPTASK
import com.example.restpoints.R
import com.example.restpoints.database.RestpointDatabase
import com.example.restpoints.entity.RestpointEntity
import com.example.restpoints.entity.RouteNameEntity
import com.example.restpoints.fragment.*
import com.example.restpoints.model.RestpointModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * MainActivity
 *
 * Notification
 * Service
 * DB
 * 구현할 예정
 */
class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID : String = "RestPoint"
    private val CHANNEL_NAME : String = "Rest Point Channel"
    private val NOTIFICATION_ID : Int = 0
    private val NOTIFICATION_TITLE : String = "고속도로 휴게소 날씨"

    private lateinit var floatingButton : FloatingActionButton
    private var pageCount : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val transaction = supportFragmentManager.beginTransaction()
        setContentView(R.layout.activity_main)
        transaction.replace(R.id.main_fragment, MainFragment())
        transaction.commit()

        // floating button
        floatingButton = findViewById(R.id.main_floating_button)

        DEFINES.POPUP.inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        DEFINES.DBTASK.DATABASE_RP = RestpointDatabase.getInstance(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.searched_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.searched_menu) {
            settingPopup(DEFINES.POPUP.isHome)
        }
        return true
    }

    private fun settingPopup(isHome: Boolean) {
        val view = DEFINES.POPUP.inflater!!.inflate(R.layout.alert_popup, null)
        val popupText : TextView = view.findViewById(R.id.alert_title)
        val transaction = supportFragmentManager.beginTransaction()
        when (isHome) {
            // home -> favorite
            true -> {
                popupText.text = DEFINES.POPUP.HONETOFAV_TEXT
                DEFINES.POPUP.isHome = false

                val alertDialog = AlertDialog.Builder(this).apply {
                    setTitle("Q.")

                    setPositiveButton("네") { dialog, which ->
                        transaction.add(R.id.main_fragment, FavoriteFragment())
                        transaction.addToBackStack(null)
                        transaction.commit()
                    }
                    setNeutralButton("아뇨", null)
                    create()
                }
                alertDialog.setView(view)
                alertDialog.show()
            }
            // favorite -> home
            else -> {
                popupText.text = DEFINES.POPUP.FAVTOHOME_TEXT
                DEFINES.POPUP.isHome = true

                val alertDialog = AlertDialog.Builder(this).apply {
                    setTitle("Q.")
                    setPositiveButton("네") {dialog, which ->
                        transaction.add(R.id.main_fragment, MainFragment())
                        transaction.addToBackStack(null)
                        transaction.commit()
                    }
                    setNeutralButton("아뇨", null)
                    create()
                }
                alertDialog.setView(view)
                alertDialog.show()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        // Notification
        sendNotification("가즈아")

        floatingButton.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            when (pageCount) {
                0 -> {
                    transaction.add(R.id.main_fragment, DestinationFragment())
                    transaction.addToBackStack(null)
                    transaction.commit()
                    pageCount++
                    floatingButton.setImageDrawable(resources.getDrawable(R.drawable.icon_calendar))
                }
                1 -> {
                    transaction.add(R.id.main_fragment, CalendarFragment())
                    transaction.addToBackStack(null)
                    transaction.commit()
                    pageCount++
                    floatingButton.setImageDrawable(resources.getDrawable(R.drawable.icon_check))
                }
                2 -> {
                    transaction.add(R.id.main_fragment, RestpointFragment())
                    transaction.addToBackStack(null)
                    transaction.commit()
                    pageCount=0
                    floatingButton.setImageDrawable(resources.getDrawable(R.drawable.icon_home))
                }
            }
        }
    }

    override fun onPause() {
        sendNotification("잘 가")
        super.onPause()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager : NotificationManager) {
        val channel = NotificationChannel(
            CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notification Channel for RestPoints App"
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun sendNotification(text : String) {
        val intent = Intent(this, SplashActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val notificationManager : NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)

        val notification = NotificationCompat.Builder(this, "RestPoint").apply {
            setSmallIcon(R.drawable.icon_symbol)
            setContentTitle(NOTIFICATION_TITLE)
            setContentText(text)
            setPriority(NotificationCompat.PRIORITY_HIGH)
            setContentIntent(pendingIntent)
        }
        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, notification.build())
        }
    }
}
