package com.kikoproject.uwidget.ui.theme

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.core.graphics.ColorUtils
import androidx.room.RoomDatabase
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kikoproject.uwidget.localdb.MainDataBase
import com.kikoproject.uwidget.models.GeneralOptions
import com.kikoproject.uwidget.utils.toStandardColor

@SuppressLint("StaticFieldLeak")
val themeAppMode = mutableStateOf(false)

@SuppressLint("StaticFieldLeak")
val themePrimaryColor = mutableStateOf(Color.Transparent)

@SuppressLint("StaticFieldLeak")
val themePrimaryVarColor = mutableStateOf(Color.Transparent)

@SuppressLint("StaticFieldLeak")
val themeSecondaryColor = mutableStateOf(Color.Transparent)

@SuppressLint("StaticFieldLeak")
val themeBackgroundColor = mutableStateOf(Color.Transparent)

@SuppressLint("StaticFieldLeak")
val themeErrorColor = mutableStateOf(Color.Transparent)

@SuppressLint("StaticFieldLeak")
val systemThemeIsEnabled = mutableStateOf(true)

@SuppressLint("StaticFieldLeak")
val monetEngineIsEnabled = mutableStateOf(true)

private val DarkColorPalette = darkColors(
    background = Background,
    primary = Main,
    primaryVariant = MainSec,
    secondary = Second
)

private val LightColorPalette = lightColors(
    background = LightBackground,
    primary = Main,
    primaryVariant = MainSec,
    secondary = Second
)

@Composable
fun UWidgetTheme(content: @Composable () -> Unit) {

    val systemUiController = rememberSystemUiController()
    val colors = Colors(
        primary = themePrimaryColor.value,
        primaryVariant = themePrimaryVarColor.value,
        secondary = themeSecondaryColor.value,
        secondaryVariant = themeSecondaryColor.value.copy(0.8f),
        surface = themeTextColor(),
        background = themeBackgroundColor.value,
        error = themeErrorColor.value,
        // Дальше не нужные цвета
        onPrimary = themePrimaryColor.value,
        onSecondary = themeSecondaryColor.value,
        onBackground = themePrimaryColor.value,
        onSurface = themeTextColor(),
        onError = themeErrorColor.value,
        isLight = false // Поскольку это динамический цвет нам все равно это темный или нет
    )

    systemUiController.setStatusBarColor(color = themeBackgroundColor.value)
    systemUiController.setNavigationBarColor(color = themeBackgroundColor.value)

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}


@Composable
fun themeTextColor(): Color {
    val darkTheme = !themeAppMode.value

    val textColor = if (darkTheme) {
        Color(0xFFEDF2F8)
    } else {
        Color(0xFF000000)
    }
    return textColor
}

@Composable
fun LaunchAppApplyColors(generalOptions: GeneralOptions) {
    if(systemThemeIsEnabled.value) { // Если включены системные цвета
        SystemThemeSet()
    }
    else{
        themePrimaryColor.value = generalOptions.Colors[MainColors.PRIMARY.value]
        themePrimaryVarColor.value = generalOptions.Colors[MainColors.PRIMARY_VAR.value]
        themeSecondaryColor.value = generalOptions.Colors[MainColors.SECONDARY.value]
        themeBackgroundColor.value = generalOptions.Colors[MainColors.BACKGROUND.value]
    }
}

@Composable
fun SystemThemeSet() {
    val darkTheme = !themeAppMode.value

    if (Build.VERSION.SDK_INT >= 31 && monetEngineIsEnabled.value) { // Включаем Monet

        themePrimaryColor.value = colorResource(android.R.color.system_accent1_400)
        themePrimaryVarColor.value = colorResource(android.R.color.system_accent1_500)
        themeSecondaryColor.value = colorResource(android.R.color.system_accent1_600)

        themeBackgroundColor.value = if (darkTheme) {
            colorResource(android.R.color.system_neutral1_900)
        } else {
            colorResource(android.R.color.system_neutral1_50)
        }
    } else { // Если меньше андроида 12
        if (darkTheme) {
            themePrimaryColor.value = DarkColorPalette.primary
            themePrimaryVarColor.value = DarkColorPalette.primaryVariant
            themeSecondaryColor.value = DarkColorPalette.secondary
            themeBackgroundColor.value = DarkColorPalette.background
        } else {
            themePrimaryColor.value = LightColorPalette.primary
            themePrimaryVarColor.value = LightColorPalette.primaryVariant
            themeSecondaryColor.value = LightColorPalette.secondary
            themeBackgroundColor.value = LightColorPalette.background
        }
    }
    themeErrorColor.value = Color.Red
}
enum class MainColors(val value: Int) {
    PRIMARY(0),
    PRIMARY_VAR(1),
    SECONDARY(2),
    BACKGROUND(3),
    ERROR(4),
    TEXT_COLOR(5)
}

enum class MainThemes(val value: Boolean) {
    LIGHT(true),
    DARK(false),
}