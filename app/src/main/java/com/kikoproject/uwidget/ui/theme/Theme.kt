package com.kikoproject.uwidget.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    background = Background,
    primary = Main,
    primaryVariant = MainSec,
    secondary = Second
)

private val LightColorPalette = lightColors(
    primary = Main,
    primaryVariant = MainSec,
    secondary = Second

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun UWidgetTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()
    var colors = /*if (Build.VERSION.SDK_INT >= 31) { // Включаем Monet
        if (darkTheme) {
            systemUiController.setStatusBarColor(color = colorResource(android.R.color.system_neutral1_900))
            systemUiController.setNavigationBarColor(color = colorResource(android.R.color.system_neutral1_900))
            darkColors(
                background = colorResource(android.R.color.system_neutral1_900),
                primary = colorResource(android.R.color.system_accent1_400),
                primaryVariant = colorResource(android.R.color.system_accent1_500),
                secondary = colorResource(android.R.color.system_accent1_800)
            )
        } else {
            systemUiController.setStatusBarColor(color = colorResource(android.R.color.system_neutral1_50))
            systemUiController.setNavigationBarColor(color = colorResource(android.R.color.system_neutral1_50))

            lightColors(
                background = colorResource(android.R.color.system_neutral1_50),
                primary = colorResource(android.R.color.system_accent1_400),
                primaryVariant = colorResource(android.R.color.system_accent1_500),
                secondary = colorResource(android.R.color.system_accent1_800)
            )
        }
    } else {*/
        if (darkTheme) {
            systemUiController.setStatusBarColor(color = Background)
            systemUiController.setNavigationBarColor(color = Background)

            DarkColorPalette
        } else {
            systemUiController.setStatusBarColor(color = Color.White)
            systemUiController.setNavigationBarColor(color = Color.White)

            LightColorPalette
        }
    //}
    colors = colors.copy(surface = themeTextColor())

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
fun themeTextColor(darkTheme: Boolean = isSystemInDarkTheme()): Color {
    val textColor = if (darkTheme) {
        Color(0xFFEDF2F8)
    } else {
        Color(0xFF000000)
    }
    return textColor
}