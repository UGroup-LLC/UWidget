package com.kikoproject.uwidget.models

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "options")
data class GeneralOptions (
    val generaOptionModel: GeneraOptionModel,
    val color: Color,
    @PrimaryKey val roomId: Int = 0,
){

}