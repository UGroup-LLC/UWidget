package com.kikoproject.uwidget.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme

@Composable
fun UWidgetTheme(content: @Composable () -> Unit) {
    val colors = Colors(
        primary = Main,
        primaryVariant = Main.copy(0.8f),
        secondary = Second,
        secondaryVariant = Second.copy(0.8f),
        background = Color.Black,
        surface = Color.Black,
        error = Color.Red,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = Color.Black,
        onSurface = Color.White,
        onError = Color.White,
    )

    MaterialTheme(
        colors = colors,
        typography = Typography,
        content = content
    )
}

enum class MainColors(val value: Int) {
    PRIMARY(0),
    PRIMARY_VAR(1),
    SECONDARY(2),
}