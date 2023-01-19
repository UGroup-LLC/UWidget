package com.kikoproject.uwidget.networking

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.util.Base64.DEFAULT
import android.util.Base64OutputStream
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.main.manager
import com.kikoproject.uwidget.objects.cards.StandardBottomSheet
import com.thin.downloadmanager.DefaultRetryPolicy
import com.thin.downloadmanager.DownloadRequest
import com.thin.downloadmanager.DownloadStatusListenerV1
import com.thin.downloadmanager.ThinDownloadManager
import okio.use
import java.io.*
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.math.ceil


private val isDownloaded = mutableStateOf(false)
private val isDownloading = mutableStateOf(false)
private val title = mutableStateOf("\uD83D\uDC7D Скачивание приложения \uD83D\uDC7D")
private val wearPercentage = mutableStateOf(0f)
private val isWearUploading = mutableStateOf(false)
private val isError = mutableStateOf(false)

@Composable
fun DownloadWearAppSheet(
    dialogVisible: MutableState<Boolean>,
    sixDialogVisible: MutableState<Boolean>
) {
    if (wearPercentage.value != 0f) {
        isWearUploading.value = !dialogVisible.value
    }
    StandardBottomSheet(dialogVisibleState = dialogVisible) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title.value,
                style = MaterialTheme.typography.titleSmall,
                fontSize = 18.sp,
            )
            Divider(
                modifier = Modifier.fillMaxWidth(0.5f),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(0.25f)
            )
            if(!isError.value) {
                if (wearPercentage.value == 0f) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .height(5.dp)
                            .clip(CircleShape)
                            .fillMaxWidth()
                    )
                } else {
                    LinearProgressIndicator(
                        progress = wearPercentage.value,
                        modifier = Modifier
                            .height(5.dp)
                            .clip(CircleShape)
                            .fillMaxWidth()
                    )
                }
                val context = LocalContext.current
                if (!isDownloading.value) {
                    val thread = Thread {
                        downloadWearFile(
                            context,
                            "https://github.com/UGroup-LLC/UWidget-WearAPKInstaller/releases/latest/download/uwidget_wear_launcher-release.zip",
                            dialogVisible,
                            sixDialogVisible
                        )
                    }
                    thread.start()
                    isDownloading.value = true
                }
            }
            else{
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_adb_error),
                    "error",
                    tint = MaterialTheme.colorScheme.error
                )
                Text(
                    "Ошибка, скачки!",
                    style = MaterialTheme.typography.titleSmall,
                )
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = {
                        isError.value = false
                        dialogVisible.value = false
                    },
                    border = BorderStroke(
                        1.dp,
                        if (isError.value) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Назад",
                        color = if (isError.value) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.padding(4.dp))
            }
        }
    }
}

fun downloadWearFile(
    context: Context, url: String,
    dialogVisible: MutableState<Boolean>,
    sixDialogVisible: MutableState<Boolean>
) {
    val sendApkFile = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path,
        "installer.zip"
    )
    sendApkFile.delete() //Удаляю файл перед скачкой


    val downloadRequest = DownloadRequest(Uri.parse(url))
        .setRetryPolicy(DefaultRetryPolicy())
        .setDestinationURI(
            Uri.parse(
                File(
                    (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).path,
                    "installer.zip"
                ).path
            )
        )
        .setPriority(DownloadRequest.Priority.HIGH)
        .setStatusListener(object : DownloadStatusListenerV1 {
            override fun onDownloadComplete(downloadRequest: DownloadRequest) {
                Log.d("TAG", "onDownloadComplete: ")
                sendInstallerToWear(
                    sixDialogVisible = sixDialogVisible,
                    dialogVisible = dialogVisible
                )
                wearPercentage.value = 0f
            }

            override fun onDownloadFailed(
                downloadRequest: DownloadRequest,
                errorCode: Int,
                errorMessage: String
            ) {
                Log.d("TAG", "onDownloadFailed: $errorCode : $errorMessage")
                title.value = "\uD83E\uDDE8 Ошибка! \uD83E\uDDE8"
                isError.value = true
            }

            override fun onProgress(
                downloadRequest: DownloadRequest,
                totalBytes: Long,
                downloadedBytes: Long,
                progress: Int
            ) {
                Log.d("TAG", "downloading: $progress")
                wearPercentage.value = progress.toFloat()/100
            }
        })
    val downloadManager = ThinDownloadManager()
    downloadManager.add(downloadRequest)

    isDownloaded.value = true
} // Тут скачка файла


private fun sendInstallerToWear(
    file: File = File(
        (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).path,
        "installer.zip"
    ),
    sixDialogVisible: MutableState<Boolean>,
    dialogVisible: MutableState<Boolean>
) {
    val isDownloadedInWear = mutableStateOf(false)
    val thread = Thread {
        val shellStream = manager.openStream("shell:")
        val fileBase64 = convertFileToBase64(file)

        shellStream.openOutputStream().use { os ->
            os.write(
                String.format(
                    "%1\$s\n",
                    "dumpsys battery set wireless 1"
                )
                    .toByteArray(StandardCharsets.UTF_8)
            )
            os.flush()
        }
        shellStream.openOutputStream().use { os ->
            os.write(
                String.format(
                    "%1\$s\n",
                    "dumpsys battery set status 2"
                )
                    .toByteArray(StandardCharsets.UTF_8)
            )
            os.flush()
        }

        shellStream.openOutputStream().use { os ->
            title.value = "\uD83D\uDEF0 Очистка tmp \uD83D\uDEF0"
            os.write(
                String.format(
                    "%1\$s\n",
                    "rm -r /data/local/tmp/*"
                ).toByteArray(StandardCharsets.UTF_8)
            )
            os.flush()

        }
        shellStream.openOutputStream().use { os ->
            title.value = "\uD83D\uDEF0 Очистка Download \uD83D\uDEF0"
            os.write(
                String.format(
                    "%1\$s\n",
                    "rm -r /storage/emulated/0/Download/*"
                ).toByteArray(StandardCharsets.UTF_8)
            )
            os.flush()

        }


        fileBase64.chunked(10000).forEachIndexed { index, it ->
            shellStream.openOutputStream().use { os ->
                wearPercentage.value = (index * it.length.toFloat() / fileBase64.length)
                title.value = "\uD83D\uDEF0 Отправка на часы инсталлера, ${
                    (wearPercentage.value * 100).toString().dropLast(3)
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

        wearPercentage.value = 0f

        title.value = "\uD83E\uDE90 Декодирование инсталлера \uD83E\uDE90"
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

        title.value = "\uD83D\uDC7E Распаковка инсталлера \uD83D\uDC7E"
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
        title.value = "\uD83C\uDF1F Установка инсталлера \uD83C\uDF1F"
        shellStream.openOutputStream().use { os ->
            os.write(
                String.format(
                    "%1\$s\n",
                    "pm install /data/local/tmp/uwidget_wear_launcher-release.apk"
                )
                    .toByteArray(StandardCharsets.UTF_8)
            )
            os.flush()
        }
        val shellLSStream = manager.openStream("shell:")
        val shellOnScreenStream = manager.openStream("shell:")


        title.value = "\uD83D\uDC7E Открытие инсталлера \uD83D\uDC7E"
        shellStream.openOutputStream().use { os ->
            os.write(
                String.format(
                    "%1\$s\n",
                    "monkey -p com.kiko.uwidget_wear_launcher -c android.intent.category.LAUNCHER 1"
                )
                    .toByteArray(StandardCharsets.UTF_8)
            )
            os.flush()
        }
        shellStream.openOutputStream().use { os ->
            os.write(
                String.format(
                    "%1\$s\n",
                    "adb shell dumpsys deviceidle whitelist +com.kiko.uwidget_wear_launcher"
                )
                    .toByteArray(StandardCharsets.UTF_8)
            )
            os.flush()
        }

        title.value = "\uD83D\uDEF0 Ожидание приложения \uD83D\uDEF0"
        val threadDownloadChecker = Thread {
            try {
                BufferedReader(InputStreamReader(shellLSStream.openInputStream())).use { reader ->
                    var s: String?
                    while (reader.readLine().also { s = it } != null) {
                        if (s?.contains("downloaded") == true) {
                            isDownloadedInWear.value = true
                            shellLSStream.close()
                            shellOnScreenStream.close()
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        threadDownloadChecker.start()

        while (!shellOnScreenStream.isClosed) {
            try {
                shellLSStream.openOutputStream().use { os ->
                    os.write(
                        String.format(
                            "%1\$s\n",
                            "ls /storage/emulated/0/Download/"
                        )
                            .toByteArray(StandardCharsets.UTF_8)
                    )
                    os.flush()
                }
                if (isDownloadedInWear.value) {
                    threadDownloadChecker.interrupt()
                    break
                }
            } catch (e: Exception) {
                Log.e("STREAM", "Closed")
            }
        }
        shellStream.openOutputStream().use { os ->
            os.write(
                String.format(
                    "%1\$s\n",
                    "dumpsys battery reset"
                )
                    .toByteArray(StandardCharsets.UTF_8)
            )
            os.flush()
        }


        shellStream.openOutputStream().use { os ->
            os.write(
                String.format(
                    "%1\$s\n",
                    "mv /storage/emulated/0/Download/update.zip /data/local/tmp/"
                )
                    .toByteArray(StandardCharsets.UTF_8)
            )
            os.flush()
        }

        title.value = "\uD83D\uDC7E Распаковка приложения \uD83D\uDC7E"
        shellStream.openOutputStream().use { os ->
            os.write(
                String.format(
                    "%1\$s\n",
                    "unzip /data/local/tmp/update.zip -d /data/local/tmp/"
                )
                    .toByteArray(StandardCharsets.UTF_8)
            )
            os.flush()
        }

        title.value = "\uD83C\uDF1F Удаление инсталлера \uD83C\uDF1F"
        shellStream.openOutputStream().use { os ->
            os.write(
                String.format("%1\$s\n", "pm uninstall com.kiko.uwidget_wear_launcher")
                    .toByteArray(StandardCharsets.UTF_8)
            )
            os.flush()
        }

        title.value = "\uD83C\uDF1F Установка приложения \uD83C\uDF1F"
        shellStream.openOutputStream().use { os ->
            os.write(
                String.format("%1\$s\n", "pm install /data/local/tmp/uwidget_wear-release.apk")
                    .toByteArray(StandardCharsets.UTF_8)
            )
            os.flush()
        }

        title.value = "\uD83D\uDC7E Открытие приложения \uD83D\uDC7E"
        shellStream.openOutputStream().use { os ->
            os.write(
                String.format(
                    "%1\$s\n",
                    "monkey -p com.kiko.uwidget_wear -c android.intent.category.LAUNCHER 1"
                )
                    .toByteArray(StandardCharsets.UTF_8)
            )
            os.flush()
        }


        title.value = "\uD83D\uDEF0 Ожидание установки \uD83D\uDEF0"

        Thread.sleep(30000)


        val updateFileZip = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path,
            "installer.zip"
        )
        updateFileZip.delete()

        sixDialogVisible.value = true
        dialogVisible.value = false
        manager.disconnect()
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