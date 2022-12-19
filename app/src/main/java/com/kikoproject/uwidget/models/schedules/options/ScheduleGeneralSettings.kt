package com.kikoproject.uwidget.models.schedules.options

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

/**
 * Класс данных отвечающий за основные настройки виджета
 *
 * @author Kiko
 */
data class ScheduleGeneralSettings(
    val backgroundColor: Color, // Цвета заднего фона виджета

    val borderColor: Color, // Цвет окантовки виджета
    val borderThickness: Byte, // Размер окантовки виджета

    val textColor: Color, // Цвет текста виджета
    val textWeight: FontWeight, // Размер текста виджета
)
