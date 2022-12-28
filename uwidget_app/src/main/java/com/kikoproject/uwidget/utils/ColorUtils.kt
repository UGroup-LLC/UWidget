package com.kikoproject.uwidget.utils

import androidx.compose.ui.graphics.Color

fun Color.toStandardColor() : Int{ // TODO("Убрать и заменить на toArgb")
    return android.graphics.Color.argb(alpha, red, green, blue)
}