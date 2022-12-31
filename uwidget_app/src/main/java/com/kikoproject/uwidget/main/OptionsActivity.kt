package com.kikoproject.uwidget.main

import android.os.Build
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import com.kikoproject.uwidget.BuildConfig
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.networking.changeMonetEngine
import com.kikoproject.uwidget.objects.BackHeader
import com.kikoproject.uwidget.objects.ListItemColor
import com.kikoproject.uwidget.objects.ListItemSwitcher
import com.kikoproject.uwidget.objects.cards.CardIllustration
import com.kikoproject.uwidget.objects.cards.RoundedCard
import com.kikoproject.uwidget.objects.watch.*
import com.kikoproject.uwidget.ui.theme.*
import com.kikoproject.uwidget.utils.toStandardColor
import io.github.muntashirakon.adb.AbsAdbConnectionManager


lateinit var manager: AbsAdbConnectionManager

/**
Окно настроек
 */
@Preview(showBackground = true)
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
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            MainOptions(scrollState)
            WearOption()
            AppInfoOptions()
        }
    }
}

@Composable
private fun AppInfoOptions() {
    Text(
        "О приложении",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
        modifier = Modifier.padding(0.dp, 15.dp, 0.dp, 0.dp)
    )
    RoundedCard(
        textColor = MaterialTheme.colorScheme.onSurface,
        text = "UWidget Version: ${BuildConfig.VERSION_NAME}(${BuildConfig.BUILD_TYPE})",
        spacing = 2.sp
    )
    RoundedCard(
        textColor = MaterialTheme.colorScheme.onSurface,
        text = "Created by UGroup 2022",
        spacing = 2.sp
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WearOption() {
    Text(
        "Часы",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
    )

    val firstStepStatus = remember { mutableStateOf(false) }
    val secondDialogVisibleState = remember { mutableStateOf(false) }
    val thirdDialogVisibleState = remember { mutableStateOf(false) }
    val fourthDialogVisibleState = remember { mutableStateOf(false) }
    val fifthDialogVisibleState = remember { mutableStateOf(false) }
    val sixDialogVisibleState = remember { mutableStateOf(false) }
    val chosenIp = remember { mutableStateOf(TextFieldValue("")) }

    CardIllustration(R.drawable.ic_undraw_watch, 3, 7)

    //region Шаги
    FirstConnectSheet(
        dialogVisibleState = firstStepStatus,
        secondDialogVisibleState = secondDialogVisibleState
    )
    SecondConnectSheet(
        dialogVisibleState = secondDialogVisibleState,
        thirdDialogVisibleState = thirdDialogVisibleState
    )
    ThirdConnectSheet(
        dialogVisibleState = thirdDialogVisibleState,
        fourthDialogVisibleState = fourthDialogVisibleState,
        chosenIp = chosenIp
    )
    FourthConnectSheet(
        dialogVisibleState = fourthDialogVisibleState,
        thirdDialogVisibleState = thirdDialogVisibleState,
        fifthDialogVisibleState = fifthDialogVisibleState,
        chosenIp = chosenIp
    )
    FifthConnectSheet(
        dialogVisibleState = fifthDialogVisibleState,
        sixDialogVisibleState = sixDialogVisibleState,
        chosenIp = chosenIp
    )
    //endregion

    OutlinedButton(
        onClick = {
            firstStepStatus.value = true
        },
        modifier = Modifier.fillMaxWidth()
    )
    {
        Text(text = "Подключить часы")
    }
}


@Composable
private fun MainOptions(scrollState: ScrollState) {
    Text(
        "Интерфейс",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
    )

    CardIllustration(R.drawable.ic_undraw_options_middle, 5, 14)

    if (systemThemeIsEnabled.value) {
        ListItemSwitcher(
            "Темная тема",
            "Включает темную тему",
            !themeAppMode.value
        ) { switchValue ->
            themeAppMode.value = !switchValue
            roomDb!!.optionsDao()
                .updateOption(roomDb!!.optionsDao().get().copy(Theme = themeAppMode.value))
        }
    }

    if (Build.VERSION.SDK_INT >= 31 && systemThemeIsEnabled.value) {
        ListItemSwitcher(
            "Движок Monet",
            "Включает цвета Android 12",
            monetEngineIsEnabled.value
        ) { switchValue ->
            monetEngineIsEnabled.value = switchValue
            changeMonetEngine(switchValue)
        }
    }

    ListItemSwitcher(
        "Системные цвета",
        "Включает системные цвета",
        systemThemeIsEnabled.value
    ) { switchValue ->
        systemThemeIsEnabled.value = switchValue
        roomDb!!.optionsDao()
            .updateOption(roomDb!!.optionsDao().get().copy(IsSystemColors = switchValue))

    }

    if (!systemThemeIsEnabled.value) {

        if (ColorUtils.calculateLuminance(themeBackgroundColor.value.toStandardColor()) < 0.5) {
            themeAppMode.value = MainThemes.DARK.value
        } else {
            themeAppMode.value = MainThemes.LIGHT.value
        }

        LaunchAppApplyColors(roomDb!!.optionsDao().get())
        LaunchedEffect(Unit) { scrollState.animateScrollTo(100) }

        ListItemColor(
            title = "Основной цвет",
            description = "Выберите основной цвет приложения",
            MainColors.PRIMARY,
            themePrimaryColor
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