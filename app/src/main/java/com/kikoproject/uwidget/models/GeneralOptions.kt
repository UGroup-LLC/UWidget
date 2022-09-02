package com.kikoproject.uwidget.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "options")
data class GeneralOptions(
    var Theme: Boolean = false,
    var OldColors: List<Color>? = null,
    val Colors: List<Color>,
    var IsSystemColors: Boolean = true,
    val IsMonetEngineEnable: Boolean = true,
    @PrimaryKey val roomId: Int = 0,
){

}