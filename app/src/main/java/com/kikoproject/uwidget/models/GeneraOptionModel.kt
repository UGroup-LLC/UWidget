package com.kikoproject.uwidget.models

import androidx.compose.ui.graphics.Color

data class GeneraOptionModel(
    var Theme: Boolean = false,
    var Colors: List<Color>? = null,
    var PrimColor: Color = Color.White
)
