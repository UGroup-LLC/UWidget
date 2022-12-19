package com.kikoproject.uwidget.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColor
import androidx.glance.text.FontWeight
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.objects.BackHeader
import com.kikoproject.uwidget.objects.ListItemColor

@Composable
fun WidgetOptionsActivity() {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        val context = LocalContext.current
        //region Переменные
        val options = roomDb?.optionsDao()?.get()
        val sOptions = options?.SchedulesOptions!!

        val generalOptions = sOptions.generalSettings
        val morningOptions = sOptions.scheduleMorningSettings
        val eveningOptions = sOptions.scheduleEveningSettings
        val dayLesionOptions = sOptions.scheduleDayLesionSettings
        val dayRestOptions = sOptions.scheduleDayRestSettings

        val backgroundColorOnError = Color(context.resources.getColor(
            R.color.statusBar
        ))
        val borderColorOnError = Color(context.resources.getColor(
            R.color.iconBack
        ))

        // Schedule General Settings
        val backgroundColor =
            remember { mutableStateOf(generalOptions.backgroundColor ?: backgroundColorOnError) }
        val borderColor =
            remember { mutableStateOf(generalOptions.borderColor ?: borderColorOnError) }
        val borderThickness =
            remember { mutableStateOf(generalOptions.borderThickness ?: 1.toByte()) }
        val textColor = remember { mutableStateOf(generalOptions.textColor ?: Color.White) }
        val textWeight =
            remember { mutableStateOf(generalOptions.textWeight ?: FontWeight.Normal) }

        // ScheduleMorningSettings
        val startMorningTime = remember { mutableStateOf(morningOptions.startMorningTime ?: 0) }
        val morningTitle = remember { mutableStateOf(morningOptions.morningTitle ?: "") }
        val morningVisible = remember { mutableStateOf(morningOptions.morningVisible ?: false) }
        val beforeLesionVisible =
            remember { mutableStateOf(morningOptions.beforeLesionVisible ?: false) }
        val nextPairVisible = remember { mutableStateOf(morningOptions.nextPairVisible ?: false) }
        val showWentTimeVisible =
            remember { mutableStateOf(morningOptions.showWentTimeVisible ?: false) }
        val showMap = remember { mutableStateOf(morningOptions.showMap ?: false) }

        // ScheduleEveningSettings
        val eveningTitleText = remember { mutableStateOf(eveningOptions.eveningTitleText ?: "") }
        val eveningTitleVisible =
            remember { mutableStateOf(eveningOptions.eveningTitleVisible ?: false) }
        val allScheduleVisible =
            remember { mutableStateOf(eveningOptions.allScheduleVisible ?: false) }
        val additionalInfoVisible =
            remember { mutableStateOf(eveningOptions.additionalInfoVisible ?: false) }

        // ScheduleDayRestSettings
        val lesionStartTitleVisible =
            remember { mutableStateOf(dayRestOptions.lesionStartTitleVisible ?: false) }
        val dayRestNextLesionVisible =
            remember { mutableStateOf(dayRestOptions.nextLesionVisible ?: false) }
        val restCountVisible =
            remember { mutableStateOf(dayRestOptions.restCountVisible ?: false) }
        val homeRestTimeVisible =
            remember { mutableStateOf(dayRestOptions.homeTimeVisible ?: false) }

        // ScheduleDayLesionSettings
        val lesionEndTitleVisible =
            remember { mutableStateOf(dayLesionOptions.lesionEndTitleVisible ?: false) }
        val dayLesionNextLesionVisible =
            remember { mutableStateOf(dayLesionOptions.nextLesionVisible ?: false) }
        val lesionCountVisible =
            remember { mutableStateOf(dayLesionOptions.lesionCountVisible ?: false) }
        val homeLesionTimeVisible =
            remember { mutableStateOf(dayLesionOptions.homeTimeVisible ?: false) }
        //endregion

        val scrollState = rememberScrollState()
        BackHeader("")
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .padding(24.dp, 0.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 12.dp),
                text = "Основные настройки",
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.primary
            )
            Text(
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 12.dp),
                text = "Цвета",
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.primary
            )
            ListItemColor(
                title = "Цвет фона",
                description = "Выберите цвет фона виджета",
                changingColor = backgroundColor,
                border = true
            ){
                roomDb?.optionsDao()?.updateOption(options.copy(SchedulesOptions = sOptions.copy(generalSettings = generalOptions.copy(backgroundColor = it))))
            }
            ListItemColor(
                title = "Вторичный цвет",
                description = "Выберите цвет обводки",
                changingColor = borderColor,
                border = true
            ){
                roomDb?.optionsDao()?.updateOption(options.copy(SchedulesOptions = sOptions.copy(generalSettings = generalOptions.copy(borderColor = it))))
            }

        }
    }
}