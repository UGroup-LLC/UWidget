package com.kikoproject.uwidget.models

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    @ColumnInfo(name = "name") val Name: String,
    @ColumnInfo(name = "surname") val Surname: String,
    @ColumnInfo(name = "avatar") val Avatar: Bitmap,
    @ColumnInfo(name = "id") val Id: String,
    @PrimaryKey val roomId: Int = 0
)