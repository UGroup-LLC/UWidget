package com.kikoproject.uwidget.models

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kikoproject.uwidget.models.schedules.options.ScheduleOptions

@Entity(tableName = "options")
data class GeneralOptions(
    var Theme: Boolean = false,
    var OldColors: List<Color>? = null,
    val Colors: List<Color>,
    var IsSystemColors: Boolean = true,
    val IsMonetEngineEnable: Boolean = true,
    val SchedulesOptions: ScheduleOptions?,
    @PrimaryKey val roomId: Int = 0,
)