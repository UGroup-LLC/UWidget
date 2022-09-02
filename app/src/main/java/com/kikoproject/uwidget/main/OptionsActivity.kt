package com.kikoproject.uwidget.main

import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import com.kikoproject.uwidget.BuildConfig
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.models.GeneralOptions
import com.kikoproject.uwidget.objects.*
import com.kikoproject.uwidget.ui.theme.*
import com.kikoproject.uwidget.utils.toStandardColor

/**
Окно настроек
 */
@Composable
fun OptionsActivity() {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        val scrollState = rememberScrollState()
        BackHeader("")
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .padding(24.dp, 0.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 12.dp),
                text = "Настройки",
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.primary
            )
            MainOptions(scrollState)

            DevicesOptions()
        }
    }
}

@Composable
private fun DevicesOptions(){
    Text(
        "О приложении",
        style = MaterialTheme.typography.h2,
        color = MaterialTheme.colors.surface.copy(alpha = 0.8f),
        modifier = Modifier.padding(0.dp,15.dp,0.dp,0.dp)
    )
    RoundedCard(textColor = MaterialTheme.colors.surface, text = "UWidget Version: ${BuildConfig.VERSION_NAME}(${BuildConfig.BUILD_TYPE})", spacing = 2.sp)
    RoundedCard(textColor = MaterialTheme.colors.surface, text = "Created by UGroup 2022", spacing = 2.sp)

}

@Composable
private fun MainOptions(scrollState: ScrollState){
    Text(
        "Основные",
        style = MaterialTheme.typography.h2,
        color = MaterialTheme.colors.surface.copy(alpha = 0.8f)
    )

    CardIllustration(R.drawable.ic_undraw_options_middle, 5, 14)

    val amoledTheme = remember { mutableStateOf(false) }

    if(systemThemeIsEnabled.value) {
        ListItemSwitcher(
            "Темная тема",
            "Включает темную тему",
            !themeAppMode.value
        ) { switchValue ->
            themeAppMode.value = !switchValue
            roomDb.optionsDao()
                .updateOption(roomDb.optionsDao().get().copy(themeAppMode.value))
        }
    }

    ListItemSwitcher(
        "Системные цвета",
        "Включает системные цвета",
        systemThemeIsEnabled.value
    ) { switchValue ->
        systemThemeIsEnabled.value = switchValue
        roomDb.optionsDao()
            .updateOption(roomDb.optionsDao().get().copy(IsSystemColors = switchValue))

    }

    if (!systemThemeIsEnabled.value) {

        if (ColorUtils.calculateLuminance(themeBackgroundColor.value.toStandardColor()) < 0.5) {
            themeAppMode.value = MainThemes.DARK.value
        } else {
            themeAppMode.value = MainThemes.LIGHT.value
        }

        LaunchAppApplyColors(roomDb.optionsDao().get())
        LaunchedEffect(Unit) { scrollState.animateScrollTo(100) }

        ListItemColor(
            title = "Основной цвет",
            description = "Выберите основной цвет приложения",
            MainColors.PRIMARY,
            themePrimaryColor
        )
        ListItemColor(
            title = "Вариантный цвет",
            description = "Выберите 2 основной цвет приложения",
            MainColors.PRIMARY_VAR,
            themePrimaryVarColor
        )
        ListItemColor(
            title = "Вторичный цвет",
            description = "Выберите вторичный цвет приложения",
            MainColors.SECONDARY,
            themeSecondaryColor
        )
        ListItemColor(
            title = "Задний цвет",
            description = "Выберите задний цвет приложения",
            MainColors.BACKGROUND,
            themeBackgroundColor,
            border = true
        )

        ListItemColor(
            title = "Цвет ошибки",
            description = "Выберите цвет ошибок приложения",
            MainColors.ERROR,
            themeErrorColor
        )
    }

}