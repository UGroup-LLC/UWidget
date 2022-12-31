package com.kikoproject.uwidget.objects.watch

import android.os.Environment
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.BuildConfig
import com.kikoproject.uwidget.main.gitReleases
import com.kikoproject.uwidget.main.manager
import com.kikoproject.uwidget.main.updateState
import com.kikoproject.uwidget.networking.*
import com.kikoproject.uwidget.objects.cards.StandardBottomSheet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FifthConnectSheet(
    dialogVisibleState: MutableState<Boolean>,
    sixDialogVisibleState: MutableState<Boolean>,
    chosenIp: MutableState<TextFieldValue>
) {
    val canExecute = remember{ mutableStateOf(true)}
    if (dialogVisibleState.value) {
        if(canExecute.value) {


            getLastWearRelease(object : Callback<GitReleases> { // Вход
                override fun onResponse(
                    call: Call<GitReleases>,
                    response: Response<GitReleases>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if ((body?.tag_name ?: "") != BuildConfig.VERSION_NAME) {
                            gitReleases.value = body
                            updateState.value = true
                            canExecute.value = false
                        }
                    }
                }

                override fun onFailure(call: Call<GitReleases>, t: Throwable) {

                }
            })
        }
    }
    if (updateState.value) {
        gitReleases.value?.let {
            val dialogVisible = remember { mutableStateOf(true) }
            DownloadWearAppSheet(
                release = it,
                manager = manager,
                dialogVisible = dialogVisible,
                secondDialogVisible = dialogVisibleState
            )
        }
    }
    StandardBottomSheet(dialogVisibleState = dialogVisibleState) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "\uD83D\uDC7D Получение информации \uD83D\uDC7D",
                style = MaterialTheme.typography.titleSmall,
                fontSize = 18.sp
            )
            Divider(
                modifier = Modifier.fillMaxWidth(0.5f),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(0.25f)
            )
            LinearProgressIndicator(
                modifier = Modifier
                    .height(5.dp)
                    .clip(CircleShape)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(4.dp))
        }
    }
}