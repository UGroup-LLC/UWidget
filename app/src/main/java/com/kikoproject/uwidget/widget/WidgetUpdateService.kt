package com.kikoproject.uwidget.widget

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.util.*

class WidgetUpdateService : Service() {
    override fun onCreate() {
        // Set up a timer to update the widget every minute
        Log.v("WidgetUpdater", "Launched")

        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runBlocking {
                    Log.v("WidgetUpdater", "Update, period = ${60000-LocalDateTime.now().second*1000L}")
                    MainWidget().updateAll(applicationContext)
                }
            }
        }, 0, 60000) // Update every minute (60000 milliseconds)
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}


class CalibrateUpdateService : Service() {
    override fun onCreate() {
        // Set up a timer to update the widget every minute
        Log.v("WidgetUpdater", "Calibration = ${60000-LocalDateTime.now().second*1000L}")

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                val intent = Intent(applicationContext, WidgetUpdateService::class.java)
                applicationContext.startService(intent)
            }
        }, 60000-LocalDateTime.now().second*1000L) // Update every minute (60000 milliseconds)
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}