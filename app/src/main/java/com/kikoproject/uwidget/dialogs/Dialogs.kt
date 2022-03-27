package com.kikoproject.uwidget.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.kikoproject.uwidget.main.navController
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.ui.theme.themeTextColor

@Composable
fun ShowLoadingDialog(state: MutableState<Boolean>) {
    val textColor = themeTextColor()
    if (state.value) {
        AlertDialog(
            properties = DialogProperties(dismissOnClickOutside = false),
            containerColor = MaterialTheme.colors.background,
            onDismissRequest = { state.value = false },
            title = {
                Text(
                    text = "Подождите, идет загрузка",
                    textAlign = TextAlign.Center,
                    color = textColor
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LinearProgressIndicator(
                        trackColor = MaterialTheme.colors.primaryVariant,
                        color = MaterialTheme.colors.primary
                    )
                }
            },
            dismissButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = {
                        state.value = false
                        navController.navigate(ScreenNav.GoogleAuthNav.route)
                    }) {
                        Text(text = "Автономный режим", color = MaterialTheme.colors.secondary)
                    }
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
            },
            confirmButton = {

            })
    }
}


@Composable
fun ShowErrorDialog(text: String) {
    val state = remember { mutableStateOf(true) }
    val textColor = themeTextColor()
    if (state.value) {
        AlertDialog(
            properties = DialogProperties(dismissOnClickOutside = false),
            containerColor = MaterialTheme.colors.background,
            onDismissRequest = { state.value = false },
            title = {
                Text(
                    text = "Ошибка! Нет интернет соединения",
                    textAlign = TextAlign.Center,
                    color = textColor
                )
            },
            text = {},
            dismissButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = {
                        state.value = false
                        navController.navigate(ScreenNav.GoogleAuthNav.route)
                    }) {
                        Text(text = text, color = MaterialTheme.colors.secondary)
                    }
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
            },
            confirmButton = {

            })
    }
}