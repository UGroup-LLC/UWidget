package com.kikoproject.uwidget.models

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "options")
data class GeneralOptions(
    var Theme: Boolean = false,
    var OldColors: List<Color>? = null,
    val Colors: List<Color>,
    var IsSystemColors: Boolean = true,
    val IsMonetEngineEnable: Boolean = true,
    @PrimaryKey val roomId: Int = 0,
)