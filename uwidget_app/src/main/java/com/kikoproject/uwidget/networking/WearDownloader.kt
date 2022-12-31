package com.kikoproject.uwidget.networking

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.util.Base64.DEFAULT
import android.util.Base64OutputStream
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.objects.cards.StandardBottomSheet
import com.kikoproject.uwidget.utils.AdbConnectionManager
import com.kikoproject.uwidget.utils.unzip
import io.github.muntashirakon.adb.AbsAdbConnectionManager
import io.github.muntashirakon.adb.LocalServices
import okio.use
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.math.ceil
import kotlin.math.round


fun getLastWearRelease(callback: Callback<GitReleases>) {
    val server = Server("https://api.github.com/repos/UGroup-LLC/UWidget-WearReleases/")

    val call: Call<GitReleases> = server.api.getLastRelease()

    call.enqueue(callback)
}

private val isDownloaded = mutableStateOf(false)
private val isZipped = mutableStateOf(false)
val title = mutableStateOf("\uD83D\uDC7D Скачивание приложения \uD83D\uDC7D")
val percentage = mutableStateOf(0f)

@Composable
fun DownloadWearAppSheet(
    release: GitReleases,
    manager: AbsAdbConnectionManager,
    dialogVisible: MutableState<Boolean>,
    secondDialogVisible: MutableState<Boolean>
) {
    StandardBottomSheet(dialogVisibleState = dialogVisible) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title.value,
                style = MaterialTheme.typography.titleSmall,
                fontSize = 18.sp
            )
            Divider(
                modifier = Modifier.fillMaxWidth(0.5f),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(0.25f)
            )
            if (percentage.value == 0f) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .height(5.dp)
                        .clip(CircleShape)
                        .fillMaxWidth()
                )
            } else {
                LinearProgressIndicator(
                    progress = percentage.value,
                    modifier = Modifier
                        .height(5.dp)
                        .clip(CircleShape)
                        .fillMaxWidth()
                )
            }
            val context = LocalContext.current
            if (isZipped.value) {
                deArchiveWearApk(manager = manager)
            }
            if (secondDialogVisible.value) {
                val thread = Thread {
                    downloadWearFile(context, release.assets[0].browser_download_url)
                    secondDialogVisible.value = false
                }
                thread.start()
            }
        }
    }
}

fun downloadWearFile(context: Context, url: String) {
    val updateFileZip = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path,
        "update.zip"
    )
    updateFileZip.delete()
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
        onCompleteWearDownload,
        IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
    );
}

private var onCompleteWearDownload: BroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(ctxt: Context?, intent: Intent?) {
        if (ctxt != null) {
            isZipped.value = true
            isDownloaded.value = false
        }
    }
}

private fun deArchiveWearApk(
    manager: AbsAdbConnectionManager,
    file: File = File(
        (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).path,
        "update.zip"
    )
) {
    isZipped.value = false
    val thread = Thread {
        val shellStream = manager.openStream("shell:")
        val fileBase64 = convertFileToBase64(file)

        shellStream.openOutputStream().use { os ->
            title.value = "\uD83D\uDEF0 Очистка \uD83D\uDEF0"
            os.write(
                String.format(
                    "%1\$s\n",
                    "rm /data/local/tmp/*"
                ).toByteArray(StandardCharsets.UTF_8)
            )
            os.flush()

        }

        fileBase64.chunked(1000).forEachIndexed { index, it ->
            shellStream.openOutputStream().use { os ->
                percentage.value = (index * it.length.toFloat() / fileBase64.length)
                title.value = "\uD83D\uDEF0 Отправка на часы, ${
                    (percentage.value * 100).toString().dropLast(3)
                }% \uD83D\uDEF0"
                os.write(
                    String.format(
                        "%1\$s\n",
                        "echo -e -n '$it' >> /data/local/tmp/uw_wear_base64.txt"
                    ).toByteArray(StandardCharsets.UTF_8)
                )
                os.flush()

            }
        }
        title.value = "\uD83E\uDE90 Декодирование \uD83E\uDE90"
        shellStream.openOutputStream().use { os ->
            os.write(
                String.format(
                    "%1\$s\n",
                    "base64 -d /data/local/tmp/uw_wear_base64.txt > /data/local/tmp/uw_wear_zip_from_base64.zip"
                )
                    .toByteArray(StandardCharsets.UTF_8)
            )
            os.flush()
        }

        title.value = "\uD83D\uDC7E Распаковка \uD83D\uDC7E"
        shellStream.openOutputStream().use { os ->
            os.write(
                String.format(
                    "%1\$s\n",
                    "unzip /data/local/tmp/uw_wear_zip_from_base64.zip -d /data/local/tmp/"
                )
                    .toByteArray(StandardCharsets.UTF_8)
            )
            os.flush()
        }
        title.value = "\uD83C\uDF1F Установка \uD83C\uDF1F"
        shellStream.openOutputStream().use { os ->
            os.write(
                String.format("%1\$s\n", "pm install /data/local/tmp/uwidget_wear-release.apk")
                    .toByteArray(StandardCharsets.UTF_8)
            )
            os.flush()
        }
        shellStream.openInputStream().use {
            val buf = it.bufferedReader().lines()
            buf
        }
    }
    thread.start()
}

fun ByteArray.split(needIterations: Int): List<ByteArray> {
    val returnArray = mutableListOf<ByteArray>()

    val splitterValue = ceil(this.size.toFloat() / needIterations.toFloat()).toInt()

    for (i: Int in 1..splitterValue) {
        if (i * needIterations <= this.size) {
            returnArray.add(this.copyOfRange((i - 1) * needIterations, i * needIterations))
        } else {
            returnArray.add(this.copyOfRange((i - 1) * needIterations, this.size))
        }
    }
    return returnArray
}

fun generateRandomByteArray(size: Int): ByteArray {
    val random = Random()
    return ByteArray(size) { random.nextInt().toByte() }
}

fun ByteArray.toHexString() = joinToString(" ") { "%02x".format(it) }

fun convertFileToBase64(file: File): String {
    return ByteArrayOutputStream().use { outputStream ->
        Base64OutputStream(outputStream, DEFAULT).use { base64FilterStream ->
            file.inputStream().use { inputStream ->
                inputStream.copyTo(base64FilterStream)
            }
        }
        return@use outputStream.toString()
    }
}

fun String.split(n: Int) = Pair(this.drop(n), this.take(n))
fun String.chunked(n: Int): Sequence<String> =
    generateSequence(this.split(n), {
        when {
            it.first.isEmpty() -> null
            else -> it.first.split(n)
        }
    })
        .map(Pair<*, String>::second)