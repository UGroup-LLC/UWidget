package com.kikoproject.uwidget.models.schedules.options

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

data class ScheduleGeneralSettings(
    val backgroundColor: Color,
    val glassEffect: Boolean,

    val borderColor: Color,
    val borderThickness: Byte,

    val textColor: Color,
    val textWeight: FontWeight,

    val textSecondaryColor: Color
)
