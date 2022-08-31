package com.kikoproject.uwidget.models

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "options")
data class GeneralOptions (
    @ColumnInfo(name = "theme") val Theme: Boolean,
    @PrimaryKey val roomId: Int = 0
)