package com.kikoproject.uwidget.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import java.time.LocalDateTime


class WidgetUpdateService : Service() {
    override fun onCreate() {
        // Set up a timer to update the widget every minute
        Log.v("WidgetUpdater", "Launched")

        val pending = PendingIntent.getBroadcast(
            this,
            0,
            Intent(applicationContext, MyWidgetReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        val manager = this.getSystemService(ALARM_SERVICE) as AlarmManager

        // set alarm to fire 5 sec (1000*5) from now (SystemClock.elapsedRealtime())
        manager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + (60000 - LocalDateTime.now().second * 1000)+15000,
            60000,
            pending
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        startService(Intent(applicationContext, this::class.java))
        super.onDestroy()
    }
}