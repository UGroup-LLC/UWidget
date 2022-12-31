package com.kikoproject.uwidget.ui.theme

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kikoproject.uwidget.models.GeneralOptions

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

private val DarkColorPalette = darkColorScheme(
    background = Background,
    primary = Main,
    primaryContainer = MainSec,
    secondary = Second
)

private val LightColorPalette = lightColorScheme(
    background = LightBackground,
    primary = Main,
    primaryContainer = MainSec,
    secondary = Second
)

@Composable
fun UWidgetTheme(content: @Composable () -> Unit) {

    val systemUiController = rememberSystemUiController()
    val colors = ColorScheme(
        primary = themePrimaryColor.value,
        onPrimary = themeTextColor(),
        primaryContainer = themePrimaryColor.value,
        onPrimaryContainer = themeBackgroundColor.value ,
        inversePrimary = Color(0xFF000000),
        secondary = themeSecondaryColor.value,
        onSecondary = themePrimaryColor.value,
        secondaryContainer = themeSecondaryColor.value,
        onSecondaryContainer = Color(0xffffffff),
        tertiary = themePrimaryColor.value,
        onTertiary = Color(0xffffffff),
        tertiaryContainer = themePrimaryColor.value,
        onTertiaryContainer = Color(0xffffffff),
        background = themeBackgroundColor.value,
        onBackground = themeTextColor(),
        surface = themeBackgroundColor.value,
        onSurface = themeTextColor(),
        surfaceVariant = themeBackgroundColor.value,
        onSurfaceVariant = themeTextColor().copy(0.4f),
        surfaceTint = themeBackgroundColor.value,
        inverseSurface = themeBackgroundColor.value,
        inverseOnSurface = Color(0xFF000000),
        error = themeErrorColor.value.copy(0.8f),
        onError = themeErrorColor.value,
        errorContainer = themeErrorColor.value.copy(0.5f),
        onErrorContainer = themeErrorColor.value,
        outline = themePrimaryColor.value,
        outlineVariant = themeBackgroundColor.value,
        scrim = Color(0xffffffff),
    )

    systemUiController.setStatusBarColor(color = themeBackgroundColor.value)
    systemUiController.setNavigationBarColor(color = themeBackgroundColor.value)

    MaterialTheme(
        colorScheme = colors,
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
            themePrimaryVarColor.value = DarkColorPalette.primaryContainer
            themeSecondaryColor.value = DarkColorPalette.secondary
            themeBackgroundColor.value = DarkColorPalette.background
        } else {
            themePrimaryColor.value = LightColorPalette.primary
            themePrimaryVarColor.value = LightColorPalette.primaryContainer
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
}

enum class MainThemes(val value: Boolean) {
    LIGHT(true),
    DARK(false),
}