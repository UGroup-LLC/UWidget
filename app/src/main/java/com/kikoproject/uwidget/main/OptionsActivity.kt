package com.kikoproject.uwidget.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.models.GeneralOptions
import com.kikoproject.uwidget.objects.BackHeader
import com.kikoproject.uwidget.objects.CardIllustration
import com.kikoproject.uwidget.objects.ListItemColor
import com.kikoproject.uwidget.objects.ListItemSwitcher
import com.kikoproject.uwidget.ui.theme.themeAppMode

/**
Окно настроек
 */
@Composable
fun OptionsActivity() {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        BackHeader("")
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(24.dp, 0.dp)
        ) {
            Text(
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 12.dp),
                text = "Настройки",
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.primary
            )
            Text(
                "Основные",
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.surface.copy(alpha = 0.8f)
            )

            CardIllustration(R.drawable.ic_undraw_options_middle, 5, 14)

            ListItemSwitcher(
                "Темная тема",
                "Включает темную тему",
                !themeAppMode.value
            ) { switchValue ->
                themeAppMode.value = !switchValue
//                roomDb.optionsDao().updateOption(roomDb.optionsDao().get().copy(themeAppMode.value))
            }
            ListItemColor(title = "Основной цвет", description = "Выберите основной цвет приложения", content = {})

        }
    }
}