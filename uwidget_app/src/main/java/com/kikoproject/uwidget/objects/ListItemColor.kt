package com.kikoproject.uwidget.objects

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import com.kikoproject.uwidget.dialogs.ColorPicker
import com.kikoproject.uwidget.networking.addOldThemeColor
import com.kikoproject.uwidget.networking.changeTheme
import com.kikoproject.uwidget.networking.changeThemeColor
import com.kikoproject.uwidget.ui.theme.MainColors
import com.kikoproject.uwidget.ui.theme.MainThemes
import com.kikoproject.uwidget.ui.theme.themeAppMode
import com.kikoproject.uwidget.utils.toStandardColor


@Composable
fun ListItemColor(
    title: String,
    description: String,
    colorType: MainColors,
    changingColor: MutableState<Color>,
    border: Boolean = false
) {
    val dialogVisible = remember {
        mutableStateOf(false)
    }

    if (dialogVisible.value) {
        ColorPicker(dialogVisible,
            oldColorsClick = { genOptions, colorValue ->
                changingColor.value = colorValue

                changeThemeColor(colorType, colorValue)
                if (colorType.value == MainColors.BACKGROUND.value) {
                    if (ColorUtils.calculateLuminance(colorValue.toStandardColor()) < 0.5) {
                        themeAppMode.value = MainThemes.DARK.value
                    } else {
                        themeAppMode.value = MainThemes.LIGHT.value
                    }
                }
                dialogVisible.value = false
            }, applyColorClick = { genOptions, colorValue ->

                addOldThemeColor(colorValue)
                changeThemeColor(colorType, colorValue)
                if (colorType.value == MainColors.BACKGROUND.value) {
                    if (ColorUtils.calculateLuminance(colorValue.toStandardColor()) < 0.5) {
                        changeTheme(MainThemes.DARK.value)
                    } else {
                        changeTheme(MainThemes.LIGHT.value)
                    }
                }
                dialogVisible.value = false
                changingColor.value = colorValue // меняемый цвет
            })
    }


    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .clickable {
                dialogVisible.value = true
            }
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
                fontSize = 16.sp,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = description,
                color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                style = MaterialTheme.typography.labelMedium
            )
        }
        Card(
            modifier = Modifier.size(35.dp, 35.dp),
            shape = CircleShape,
            border = if (border) {
                BorderStroke(1.dp, color = MaterialTheme.colorScheme.onSurface)
            } else {
                BorderStroke(0.dp, color = Color.Transparent)
            },
            colors = CardDefaults.cardColors(containerColor = changingColor.value),
        ) {

        }
    }
}

@Composable
fun ListItemColor(
    title: String,
    description: String,
    changingColor: MutableState<Color>,
    border: Boolean = false,
    onChangeColor: (color: Color) -> Unit
) {
    val dialogVisible = remember {
        mutableStateOf(false)
    }

    if (dialogVisible.value) {
        ColorPicker(dialogVisible,
            oldColorsClick = { genOptions, colorValue ->
                changingColor.value = colorValue
                dialogVisible.value = false
                onChangeColor(colorValue)
            }, applyColorClick = { genOptions, colorValue ->
                dialogVisible.value = false
                changingColor.value = colorValue // меняемый цвет
                onChangeColor(colorValue)
            })
    }


    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .clickable {
                dialogVisible.value = true
            }
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = description,
                color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                style = MaterialTheme.typography.labelMedium
            )
        }
        Card(
            modifier = Modifier.size(35.dp, 35.dp),
            shape = CircleShape,
            border = if (border) {
                BorderStroke(1.dp, color = MaterialTheme.colorScheme.onSurface)
            } else {
                BorderStroke(0.dp, color = Color.Transparent)
            },
            colors = CardDefaults.cardColors(containerColor = changingColor.value),
        ) {

        }
    }
}