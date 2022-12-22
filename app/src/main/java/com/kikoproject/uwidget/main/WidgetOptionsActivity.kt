package com.kikoproject.uwidget.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.appwidget.updateAll
import androidx.glance.text.FontWeight
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.objects.BackHeader
import com.kikoproject.uwidget.objects.ListItemColor
import com.kikoproject.uwidget.widget.MainWidget
import com.smarttoolfactory.slider.MaterialSliderDefaults
import com.smarttoolfactory.slider.SliderBrushColor
import com.smarttoolfactory.slider.SliderWithLabel
import kotlinx.coroutines.runBlocking

@Composable
fun WidgetOptionsActivity() {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        val context = LocalContext.current
        //region Переменные
        var options = roomDb?.optionsDao()?.get()
        var sOptions = options?.SchedulesOptions!!

        val generalOptions = sOptions.generalSettings
        val morningOptions = sOptions.scheduleMorningSettings
        val eveningOptions = sOptions.scheduleEveningSettings
        val dayLesionOptions = sOptions.scheduleDayLesionSettings
        val dayRestOptions = sOptions.scheduleDayRestSettings

        val backgroundColorOnError = Color(
            context.resources.getColor(
                R.color.statusBar
            )
        )
        val borderColorOnError = Color(
            context.resources.getColor(
                R.color.iconBack
            )
        )


        // Schedule General Settings
        val backgroundColor =
            remember { mutableStateOf(generalOptions.backgroundColor ?: backgroundColorOnError) }
        val borderColor =
            remember { mutableStateOf(generalOptions.borderColor ?: borderColorOnError) }
        val borderThickness =
            remember { mutableStateOf((generalOptions.borderThickness / 5).toByte()) }
        val textColor = remember { mutableStateOf(generalOptions.textColor ?: Color.White) }
        val textSize =
            remember { mutableStateOf((generalOptions.textSize) ?: 1.toByte()) }
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
        fun updateWidgetData() {
            roomDb?.optionsDao()?.updateOption(
                roomDb?.optionsDao()?.get()!!.copy(
                    SchedulesOptions = sOptions.copy(
                        generalSettings = generalOptions.copy(
                            backgroundColor = backgroundColor.value,
                            borderColor = borderColor.value,
                            borderThickness = (borderThickness.value.toInt() * 5).toByte(),
                            textColor = textColor.value,
                            textSize = textSize.value
                        ),
                        scheduleMorningSettings = morningOptions.copy(
                            startMorningTime = startMorningTime.value,
                            morningTitle = morningTitle.value,
                            morningVisible = morningVisible.value,
                            beforeLesionVisible = beforeLesionVisible.value,
                            nextPairVisible = nextPairVisible.value,
                            showWentTimeVisible = showWentTimeVisible.value,
                            showMap = showMap.value
                        ),
                        scheduleDayLesionSettings = dayLesionOptions.copy(
                            lesionEndTitleVisible = lesionEndTitleVisible.value,
                            nextLesionVisible = dayLesionNextLesionVisible.value,
                            lesionCountVisible = lesionCountVisible.value,
                            homeTimeVisible = homeLesionTimeVisible.value
                        ),
                        scheduleDayRestSettings = dayRestOptions.copy(
                            lesionStartTitleVisible = lesionStartTitleVisible.value,
                            nextLesionVisible = dayRestNextLesionVisible.value,
                            restCountVisible = restCountVisible.value,
                            homeTimeVisible = homeRestTimeVisible.value
                        ),
                        scheduleEveningSettings = eveningOptions.copy(
                            eveningTitleText = eveningTitleText.value,
                            eveningTitleVisible = eveningTitleVisible.value,
                            allScheduleVisible = allScheduleVisible.value,
                            additionalInfoVisible = additionalInfoVisible.value
                        )
                    )

                )
            )
            runBlocking {
                MainWidget().updateAll(context)
            }
        }

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
            ) {
                updateWidgetData()
            }
            ListItemColor(
                title = "Вторичный цвет",
                description = "Выберите цвет обводки",
                changingColor = borderColor,
                border = true
            ) {
                updateWidgetData()
            }
            //region borderThickness slider
            LazySlider(value = borderThickness,text = "Ширина обводки виджета", range = 0f..5f) {
                updateWidgetData()
            }
            //endregion
            //region textSize slider
            LazySlider(value = textSize,text = "Размер текста виджета" ,range = 8f..24f) {
                updateWidgetData()
            }
            //endregion
        }

    }
}

@Composable
private fun LazySlider(value: MutableState<Byte>,text: String, range: ClosedFloatingPointRange<Float>,content: () -> Unit) {
    val hideLabel = remember {
        mutableStateOf(true)
    }
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = text,
            color = MaterialTheme.colors.surface.copy(0.8f),
            fontSize = 16.sp
        )
        SliderWithLabel(
            value = value.value.toFloat(), onValueChange = {
                hideLabel.value = false
                value.value =
                    it.toInt().toByte()
            },
            valueRange = range, steps = (range.endInclusive-1).toInt(),
            colors = MaterialSliderDefaults.materialColors(
                thumbColor = SliderBrushColor(color = MaterialTheme.colors.primary),
                activeTrackColor = SliderBrushColor(color = MaterialTheme.colors.primary)
            ),
            onValueChangeFinished = {
                hideLabel.value = true
                content()
            },
            label = {
                if (!hideLabel.value) {
                    Text(
                        text = "${value.value}",
                        modifier = Modifier
                            .background(MaterialTheme.colors.primary, shape = CircleShape)
                            .padding(5.dp),
                        color = Color.White
                    )
                }
            }
        )
    }
}