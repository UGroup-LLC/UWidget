package com.kikoproject.uwidget.objects.watch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.objects.cards.StandardBottomSheet
import com.kikoproject.uwidget.objects.text.colorize


@Composable
fun SecondConnectSheet(
    dialogVisibleState: MutableState<Boolean>,
    thirdDialogVisibleState: MutableState<Boolean>
) {
    StandardBottomSheet(dialogVisibleState = dialogVisibleState) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "\uD83D\uDE80 Второй шаг \uD83D\uDE80",
                style = MaterialTheme.typography.titleSmall,
                fontSize = 18.sp
            )
            Divider(
                modifier = Modifier.fillMaxWidth(0.5f),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(0.25f)
            )
            Text(
                text = "@@Включите настройки разработчика на часах, откройте @Настройки@ -> @О Часах@ -> Нажимайте на @Номер сборки@ много раз".colorize(),
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                text = "@@\nДалее вернитесь в @Настройки@ -> @Настройки разработчика@ -> Включите @Отладка ADB@ -> Включите @Отладка по Wi-Fi@".colorize(),
                style = MaterialTheme.typography.titleSmall,
            )
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = {
                    thirdDialogVisibleState.value = true
                    dialogVisibleState.value = false
                }) {
                Text(
                    text = "Выполнено",
                )
            }
        }
    }
}