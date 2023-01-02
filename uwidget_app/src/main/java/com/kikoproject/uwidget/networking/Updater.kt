package com.kikoproject.uwidget.networking

import android.app.DownloadManager
import android.content.*
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.kikoproject.uwidget.BuildConfig
import com.kikoproject.uwidget.main.updateState
import com.kikoproject.uwidget.objects.cards.StandardBottomSheet
import com.kikoproject.uwidget.objects.text.colorize
import com.kikoproject.uwidget.utils.unzip
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET
import java.io.File
import java.util.*


fun appCheckUpdate(callback: Callback<GitReleases>) {
    val server = Server("https://api.github.com/repos/UGroup-LLC/UWidget-Releases/")

    val call: Call<GitReleases> = server.api.getLastRelease()

    call.enqueue(callback)
}

interface API {
    @GET("releases/latest")
    fun getLastRelease(): Call<GitReleases>
}

data class GitReleases(
    val tag_name: String,
    val body: String,
    val assets: List<GitAssets>
) {
    constructor() : this("", "", listOf(GitAssets()))
}

data class GitAssets(
    val url: String,
    val browser_download_url: String,
    val size: Int
) {
    constructor() : this("", "", 0)
}

private val isDownloaded = mutableStateOf(false)
private val isZipped = mutableStateOf(false)

@Composable
fun NewVersionSheet(release: GitReleases) {
    val tmpFileZip = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path,
        "app-release.apk"
    )
    tmpFileZip.delete()
    val updateFileZip = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path,
        "update.zip"
    )
    updateFileZip.delete()

    StandardBottomSheet(dialogVisibleState = updateState) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "✨Вышла новая версия ${release.tag_name}✨",
                style = MaterialTheme.typography.titleSmall,
                fontSize = 18.sp
            )
            Divider(
                modifier = Modifier.fillMaxWidth(0.5f),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(0.25f)
            )
            Text(
                text = "@✍Заметки релиза✍@\n ${release.body}".colorize(),
                style = MaterialTheme.typography.titleSmall,
            )


            val context = LocalContext.current
            if (!isDownloaded.value && !isZipped.value) {
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = {
                        downloadFile(
                            context = context,
                            release.assets.first().browser_download_url
                        )
                    }) {
                    Text(
                        text = "Установить",
                    )
                }
            } else {
                LinearProgressIndicator(
                    modifier = Modifier
                        .height(5.dp)
                        .clip(CircleShape)
                        .fillMaxWidth()
                )
            }
            if (isZipped.value) {
                deArchiveApk(context = context)
            }
        }
    }
}

private fun downloadFile(context: Context, url: String) {
    val request = DownloadManager.Request(Uri.parse(url))
        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        .setTitle("Update")
        .setDescription("Download Update")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setAllowedOverMetered(true)
        .setAllowedOverRoaming(false)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "update.zip")
    val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager?
    downloadManager?.enqueue(request)

    isDownloaded.value = true
    context.registerReceiver(
        onCompleteDownload,
        IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
    );
}

var onCompleteDownload: BroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(ctxt: Context?, intent: Intent?) {
        if (ctxt != null) {
            isZipped.value = true
            isDownloaded.value = false
        }
    }
}

fun deArchiveApk(
    context: Context,
    file: File = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
) {
    file.unzip("update.zip") {


        val tmpFile = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path,
            "app-release.apk"
        )
        val fileUri = FileProvider.getUriForFile(
            context.applicationContext,
            BuildConfig.APPLICATION_ID + ".provider", tmpFile
        )
        val intent = Intent(Intent.ACTION_VIEW, fileUri)
        intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
        intent.setDataAndType(fileUri, "application/vnd.android" + ".package-archive")
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(intent)
        isZipped.value = false
    }
}