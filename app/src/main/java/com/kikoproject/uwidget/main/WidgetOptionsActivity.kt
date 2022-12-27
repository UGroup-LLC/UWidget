package com.kikoproject.uwidget.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.glance.appwidget.updateAll
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.objects.BackHeader
import com.kikoproject.uwidget.objects.ListItemColor
import com.kikoproject.uwidget.objects.ListItemSwitcher
import com.kikoproject.uwidget.objects.home_widget.ScheduleHomeWidgetPreviewDialog
import com.kikoproject.uwidget.ui.theme.systemThemeIsEnabled
import com.kikoproject.uwidget.widget.MainWidget
import com.smarttoolfactory.slider.MaterialSliderDefaults
import com.smarttoolfactory.slider.SliderBrushColor
import com.smarttoolfactory.slider.SliderWithLabel
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt

@Composable
fun WidgetOptionsActivity() {
    ScheduleHomeWidgetPreviewDialog {
        MainWidgetOptionsContent()
    }
}

@Composable
private fun MainWidgetOptionsContent() {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        val context = LocalContext.current
        //region Переменные

        val generalOptions = options!!.generalSettings
        val morningOptions = options!!.scheduleMorningSettings
        val eveningOptions = options!!.scheduleEveningSettings
        val dayLesionOptions = options!!.scheduleDayLesionSettings
        val dayRestOptions = options!!.scheduleDayRestSettings

        val backgroundColorOnError = Color(
            ContextCompat.getColor(
                context,
                R.color.statusBar
            )
        )
        val borderColorOnError = Color(
            ContextCompat.getColor(
                context,
                R.color.iconBack
            )
        )


        // Schedule General Settings
        val backgroundColor =
            remember { mutableStateOf(generalOptions.backgroundColor) }
        val borderColor =
            remember { mutableStateOf(generalOptions.borderColor) }
        val borderThickness =
            remember { mutableStateOf((generalOptions.borderThickness / 5).toByte()) }
        val isBorderVisible =
            remember { mutableStateOf((generalOptions.isBorderVisible)) }
        val textSize =
            remember { mutableStateOf((generalOptions.textSize)) }
        // ScheduleMorningSettings
        val startMorningTime = remember { mutableStateOf(morningOptions.startMorningTime) }
        val morningTitle = remember { mutableStateOf(morningOptions.morningTitle) }
        val morningVisible = remember { mutableStateOf(morningOptions.morningVisible) }
        val beforeLesionVisible =
            remember { mutableStateOf(morningOptions.beforeLesionVisible) }
        val nextPairVisible = remember { mutableStateOf(morningOptions.nextPairVisible) }
        val showWentTimeVisible =
            remember { mutableStateOf(morningOptions.showWentTimeVisible) }
        val showMap = remember { mutableStateOf(morningOptions.showMap) }

        // ScheduleEveningSettings
        val eveningTitleText = remember { mutableStateOf(eveningOptions.eveningTitleText) }
        val eveningTitleVisible =
            remember { mutableStateOf(eveningOptions.eveningTitleVisible) }
        val allScheduleVisible =
            remember { mutableStateOf(eveningOptions.allScheduleVisible) }
        val additionalInfoVisible =
            remember { mutableStateOf(eveningOptions.additionalInfoVisible) }

        // ScheduleDayRestSettings
        val lesionStartTitleVisible =
            remember { mutableStateOf(dayRestOptions.lesionStartTitleVisible) }
        val dayRestNextLesionVisible =
            remember { mutableStateOf(dayRestOptions.nextLesionVisible) }
        val restCountVisible =
            remember { mutableStateOf(dayRestOptions.restCountVisible) }
        val homeRestTimeVisible =
            remember { mutableStateOf(dayRestOptions.homeTimeVisible) }

        // ScheduleDayLesionSettings
        val lesionEndTitleVisible =
            remember { mutableStateOf(dayLesionOptions.lesionEndTitleVisible) }
        val dayLesionNextLesionVisible =
            remember { mutableStateOf(dayLesionOptions.nextLesionVisible) }
        val lesionCountVisible =
            remember { mutableStateOf(dayLesionOptions.lesionCountVisible) }
        val homeLesionTimeVisible =
            remember { mutableStateOf(dayLesionOptions.homeTimeVisible) }

        //endregion
        fun updateWidgetData() {
            roomDb?.optionsDao()?.updateOption(
                roomDb?.optionsDao()?.get()!!.copy(
                    SchedulesOptions = options!!.copy(
                        generalSettings = generalOptions.copy(
                            backgroundColor = backgroundColor.value,
                            borderColor = borderColor.value,
                            borderThickness = (borderThickness.value.toInt() * 5).toByte(),
                            isBorderVisible = isBorderVisible.value,
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
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 12.dp),
                text = "Цвета",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
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
            LazySlider(
                value = borderThickness,
                text = "Ширина обводки виджета",
                range = 0f..5f,
                updatingContent = {
                    options = options!!.copy(
                        options!!.generalSettings.copy(borderThickness = (borderThickness.value * 5).toByte()),
                        options!!.scheduleMorningSettings,
                        options!!.scheduleDayLesionSettings,
                        options!!.scheduleDayRestSettings,
                        options!!.scheduleEveningSettings
                    )
                }) {
                updateWidgetData()
            }
            //endregion
            //region textSize slider
            LazySlider(
                value = textSize,
                text = "Размер текста виджета",
                range = 8f..24f,
                updatingContent = {
                    options = options!!.copy(
                        options!!.generalSettings.copy(textSize = textSize.value),
                        options!!.scheduleMorningSettings,
                        options!!.scheduleDayLesionSettings,
                        options!!.scheduleDayRestSettings,
                        options!!.scheduleEveningSettings
                    )
                })
            {
                updateWidgetData()
            }

            ListItemSwitcher(
                "Обводка",
                "Включает обводку виджета",
                isBorderVisible.value
            ) { switchValue ->
                isBorderVisible.value = switchValue
                updateWidgetData()
            }

            //endregion

        }
    }
}

@Composable
private fun LazySlider(
    value: MutableState<Byte>,
    text: String,
    range: ClosedFloatingPointRange<Float>,
    updatingContent: () -> Unit = {},
    content: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val hideLabel = remember {
        mutableStateOf(true)
    }
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
            style = MaterialTheme.typography.labelMedium
        )
        SliderWithLabel(
            value = value.value.toFloat(), onValueChange = {
                if (value.value.toInt() != it.roundToInt()) {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }
                hideLabel.value = false
                value.value =
                    it.roundToInt().toByte()
                updatingContent()
            },
            valueRange = range, steps = (range.endInclusive - 1).toInt(),
            colors = MaterialSliderDefaults.materialColors(
                thumbColor = SliderBrushColor(color = MaterialTheme.colorScheme.primary),
                activeTrackColor = SliderBrushColor(color = MaterialTheme.colorScheme.primary)
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
                            .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                            .padding(5.dp),
                        color = Color.White
                    )
                }
            }
        )
    }
}