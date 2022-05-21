package com.kikoproject.uwidget.models

import android.graphics.Bitmap

data class User (
    val Name: String,
    val Surname: String,
    val Avatar: Bitmap,
    val Id: String
)