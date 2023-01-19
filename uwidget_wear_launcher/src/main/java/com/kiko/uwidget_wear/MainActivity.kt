package com.kiko.uwidget_wear

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.thin.downloadmanager.DefaultRetryPolicy
import com.thin.downloadmanager.DownloadRequest
import com.thin.downloadmanager.DownloadStatusListenerV1
import com.thin.downloadmanager.ThinDownloadManager
import java.io.File
import kotlin.system.exitProcess

@SuppressLint("StaticFieldLeak")
lateinit var progressBar: CircularProgressBar
@SuppressLint("StaticFieldLeak")
lateinit var textView: TextView

class MainActivity : ComponentActivity() {
    @SuppressLint("PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("DOWNLOAD", "Скачка")
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.main)
        progressBar = findViewById(R.id.progressbar)
        textView= findViewById(R.id.progressbar_text)
        downloadWearFile(
            context = applicationContext,
            "https://github.com/UGroup-LLC/UWidget-WearReleases/releases/latest/download/uwidget_wear-release.zip"
        )
    }
}

fun downloadWearFile(context: Context, url: String) {
    val downloadRequest = DownloadRequest(Uri.parse(url))
        .setRetryPolicy(DefaultRetryPolicy())
        .setDestinationURI(
            Uri.parse(
                File(
                    (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).path,
                    "update.zip"
                ).path
            )
        )
        .setPriority(DownloadRequest.Priority.HIGH)
        .setStatusListener(object : DownloadStatusListenerV1 {
            override fun onDownloadComplete(downloadRequest: DownloadRequest) {


                Log.d("TAG", "onDownloadComplete: ")
                var i = 0
                while (!File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "//downloaded$i"
                    ).createNewFile()
                ) {
                    i++
                    Log.d("CREATE", "FILE $i")
                }
                exitProcess(0)
            }

            @SuppressLint("SetTextI18n")
            override fun onDownloadFailed(
                downloadRequest: DownloadRequest,
                errorCode: Int,
                errorMessage: String
            ) {
                Log.d("TAG", "onDownloadFailed: $errorCode : $errorMessage")
                progressBar.progressBarColor = Color.RED
                textView.setTextColor(Color.RED)
                textView.text = "Ошибка!\n $errorMessage"
            }

            @SuppressLint("SetTextI18n")
            override fun onProgress(
                downloadRequest: DownloadRequest,
                totalBytes: Long,
                downloadedBytes: Long,
                progress: Int
            ) {
                Log.d("TAG", "downloading: $progress")
                progressBar.progress = progress.toFloat()
                textView.text = "${progress}%"
            }
        })
    val downloadManager = ThinDownloadManager()
    downloadManager.add(downloadRequest)
}
