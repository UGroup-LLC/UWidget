package com.kikoproject.uwidget.objects.watch

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.main.manager
import com.kikoproject.uwidget.networking.DownloadWearAppSheet
import com.kikoproject.uwidget.objects.cards.StandardBottomSheet
import com.kikoproject.uwidget.utils.AdbConnectionManager
import java.io.IOException
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FourthConnectSheet(
    dialogVisibleState: MutableState<Boolean>,
    thirdDialogVisibleState: MutableState<Boolean>,
    fifthDialogVisibleState: MutableState<Boolean>,
    chosenIp: MutableState<TextFieldValue>
) {
    val context = LocalContext.current
    val isError = remember { mutableStateOf(false) }

    val isConnectedADB = remember {
        mutableStateOf(false)
    }
//    managerAdb = ADB.getInstance(context)
    if (dialogVisibleState.value) {
        val thread = Thread {
//            managerAdb.initServer()

              try {
                  manager = AdbConnectionManager.getInstance(context)!!
                  manager.setTimeout(5, TimeUnit.SECONDS)
                  val connect =  manager.connect(chosenIp.value.text, 5555)
                  if (connect) {
                    isConnectedADB.value = true
                    dialogVisibleState.value = false
                    fifthDialogVisibleState.value = true
                    Thread.currentThread().interrupt()
                } else {
                    throw IOException("TIMEOUT")
                }
            } catch (th: IOException) {
                isError.value = true
                Thread.currentThread().interrupt()
            }

        }
        thread.start()
    }


    StandardBottomSheet(dialogVisibleState = dialogVisibleState) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (!isError.value) "\uD83D\uDCAB Ожидание подключения \uD83D\uDCAB" else "\uD83E\uDDE8 Ошибка! \uD83E\uDDE8",
                style = MaterialTheme.typography.titleSmall,
                fontSize = 18.sp
            )
            Divider(
                modifier = Modifier.fillMaxWidth(0.5f),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(0.25f)
            )
            if (!isError.value) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .height(5.dp)
                        .clip(CircleShape)
                        .fillMaxWidth()
                )
            } else {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_adb_error),
                    "error",
                    tint = MaterialTheme.colorScheme.error
                )
                Text(
                    "Ошибка, возможно вы выбрали не правильный IP",
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = {
                    thirdDialogVisibleState.value = true
                    isError.value = false
                    dialogVisibleState.value = false
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