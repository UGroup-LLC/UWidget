package com.kiko.uwidget_wear

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import java.io.File
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("DOWNLOAD", "Скачка")
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        val draw = GradientDrawable()
        draw.setColor(Color.parseColor("#181b20"))
        window.setBackgroundDrawable(draw)
        registerReceiver(
            onCompleteWearDownload,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
        if (!File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path,
                "update.zip"
            ).exists()
        ) {
            downloadWearFile(
                context = applicationContext,
                "https://github.com/UGroup-LLC/UWidget-WearReleases/releases/download/0.0.1preAlpha/uwidget_wear-release.zip"
            )
        }
        else{
            exitProcess(0)
        }
    }
}

private var onCompleteWearDownload: BroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(ctxt: Context?, intent: Intent?) {
        var i = 0
        while(!File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "//downloaded$i"
        ).createNewFile()){
            i++
            Log.d("CREATE", "FILE $i")
        }
        exitProcess(0)
    }
}

fun downloadWearFile(context: Context, url: String) {

    val request = DownloadManager.Request(Uri.parse(url))
        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        .setTitle("Download APK")
        .setDescription("Download full apk")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setAllowedOverMetered(true)
        .setAllowedOverRoaming(false)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "update.zip")
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
    downloadManager?.enqueue(request)
}
