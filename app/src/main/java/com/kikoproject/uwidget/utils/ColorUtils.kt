package com.kikoproject.uwidget.utils

import androidx.compose.ui.graphics.Color

fun Color.toStandardColor() : Int{
    return android.graphics.Color.argb(alpha, red, green, blue)
}