package com.kikoproject.uwidget.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.runBlocking

class MyWidgetReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.v("Updater", "Updated")
        runBlocking {
            MainWidget().updateAll(context)
        }
    }
}

class OnScreenReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action == Intent.ACTION_SCREEN_ON) {
            Log.v("Updater", "Updated")
            runBlocking {
                MainWidget().updateAll(context)
            }
        }
    }
}