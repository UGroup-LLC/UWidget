package com.kikoproject.uwidget.reciever

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.kikoproject.uwidget.widget.WidgetUpdateService


class MyWidgetUpdateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val service = Intent(context, WidgetUpdateService::class.java)

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Start the service when the system is rebooted
            try {
                context.startService(service)
            }catch (exception: Exception){
                Log.v("Widget","Caught service error")
            }
        } else if (intent.action == AppWidgetManager.ACTION_APPWIDGET_ENABLED) {
            // Start the service when the widget is created
            try {
                context.startService(service)
            }catch (exception: Exception){
                Log.v("Widget","Caught service error")
            }
        }
    }
}
