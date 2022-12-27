package com.kikoproject.uwidget.dialogs

import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.content.Context
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.holix.android.bottomsheetdialog.compose.BottomSheetBehaviorProperties
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties
import com.kikoproject.uwidget.main.*
import com.kikoproject.uwidget.models.GeneralOptions
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.objects.ScheduleBand
import com.kikoproject.uwidget.objects.cards.RoundedCard
import com.kikoproject.uwidget.objects.cards.StandardBottomSheet
import com.kikoproject.uwidget.utils.ScheduleGetterSelectors
import com.kikoproject.uwidget.utils.getSelectors


/**
 * Показывает диалог загрузки
 *
 * @param state показывает или прячет диалог
 */
@Composable
fun ShowLoadingDialog(state: MutableState<Boolean>) {
    if (state.value) {
        AlertDialog(
            properties = DialogProperties(dismissOnClickOutside = false),
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = { state.value = false },
            title = {
                Text(
                    text = "Подождите, идет загрузка",
                    textAlign = TextAlign.Center,
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
                        trackColor = MaterialTheme.colorScheme.primaryContainer,
                        color = MaterialTheme.colorScheme.primary
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
                        Text(text = "Автономный режим", color = MaterialTheme.colorScheme.secondary)
                    }
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            confirmButton = {

            })
    }
}

/**
 * Показывает WEB диалог который при копировании текста выбрасывает результат что было скопировано
 * @param state показывает или прячет диалог
 * @param url ссылка на сайт для отображения
 * @param result результат скопированного текста
 *
 * @author Kiko
 */
@SuppressLint("MutableCollectionMutableState")
@Composable
fun ShowSearchSelector(state: MutableState<Boolean>, url: String, result: ScheduleDialogSelector) {
    val found = remember { mutableStateOf(Pair(mutableListOf<String>(), mutableListOf<String>())) }
    val scope = rememberCoroutineScope()

    if (state.value) {
        val loadingState = remember { mutableStateOf(false) }
        val foundState = remember { mutableStateOf(false) }

        if (foundState.value) {
            ShowFoundResult(
                state = foundState,
                foundCollection = found.value,
                object : ScheduleDialogSelector {
                    override fun onResult(scheduleCSS: String) {
                        result.onResult(scheduleCSS)
                        state.value = false
                    }

                })
        }
        ShowLoadingDialog(state = loadingState)

        Dialog(onDismissRequest = { state.value = false }, content = {
            WebPageScreen(urlToRender = url)
        })

        val clipManager =
            LocalContext.current.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        clipManager.addPrimaryClipChangedListener {
            loadingState.value = true
            getSelectors(
                scope,
                url,
                clipManager.primaryClip!!.getItemAt(0).text.toString(),
                object : ScheduleGetterSelectors {
                    override fun onResult(schedules: Pair<MutableList<String>, MutableList<String>>) {
                        found.value = schedules
                        loadingState.value = false
                        foundState.value = true
                    }
                })
        }

    }
}

/**
 * Показывает найденные совпадения в ShowSearchSelectors диалоге
 *
 * @param state показывает или прячет диалог
 * @param foundCollection массив найденных совпадений на сайте по скопированному тексту
 * @param result результат селектора что было выбрано в диалоге
 *
 * @author Kiko
 */
@Composable
fun ShowFoundResult(
    state: MutableState<Boolean>,
    foundCollection: Pair<List<String>, List<String>>,
    result: ScheduleDialogSelector
) {
    if (state.value) {
        AlertDialog(
            properties = DialogProperties(dismissOnClickOutside = false),
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = { state.value = false },
            title = {
                Text(
                    text = "Надены совпадения",
                    textAlign = TextAlign.Center
                )
            },
            text = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(foundCollection.first) { index, foundItem ->
                        Button(
                            onClick = {
                                result.onResult(foundCollection.second[index])
                                state.value = false
                            }, modifier = Modifier.padding(bottom = 10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(text = foundItem, color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            },
            dismissButton = {

            },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            confirmButton = {
            })
    }
}


/**
 * Показывает диалог ошибки
 *
 * @param text текст диалога
 * @param needButton отображение кнопки
 *
 * @author Kiko
 */
@Composable
fun ShowErrorDialog(text: String, needButton: Boolean) {
    val state = remember { mutableStateOf(true) }
    val textColor = MaterialTheme.colorScheme.onSurface
    if (state.value) {
        AlertDialog(
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            ),
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = { state.value = false },
            title = {
                Text(
                    text = text,
                    textAlign = TextAlign.Center,
                    color = textColor
                )
            },
            text = {},
            dismissButton = {
                if (needButton) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = {
                            state.value = false
                            navController.navigate(ScreenNav.GoogleAuthNav.route)
                        }) {
                            Text(text = text, color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            confirmButton = {

            })
    }
}

/**
 * Информативный диалог
 *
 * @param text текст диалога
 * @param buttonText текст кнопки
 * @param content описывает что должна делать кнопка при нажатии
 *
 * @author Kiko & Levosllavny
 */
@Composable
fun ShowInfoDialog(text: String, buttonText: String, content: () -> Unit) {
    val state = remember { mutableStateOf(true) }
    if (state.value) {
        AlertDialog(
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            ),
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = { state.value = false },
            title = {
                Text(
                    text = text,
                    textAlign = TextAlign.Center
                )
            },
            text = {},
            dismissButton = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Button(onClick = {
                        content()
                    }) {
                        Text(text = buttonText, color = MaterialTheme.colorScheme.secondary)
                    }
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            confirmButton = {

            })
    }
}

/**
 * Результат выбора селектора
 *
 * @author Kiko
 */
interface ScheduleDialogSelector {
    fun onResult(scheduleCSS: String)
}

/**
 * Вьюшка для отображения веб-страницы
 *
 * @param urlToRender ссылка на сайт для отображения
 *
 * @author Kiko
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebPageScreen(urlToRender: String) {
    Box(modifier = Modifier.clip(RoundedCornerShape(15.dp))) {
        AndroidView(factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadUrl(urlToRender)
            }
        }, update = {
            it.loadUrl(urlToRender)
        }, modifier = Modifier.size(500.dp))
    }
}

/**
 * Диалог выбора цвета
 *
 * @param dialogVisibleState видимость диалога
 * @param oldColorsClick старые цвета
 * @param applyColorClick цвет который был применен
 *
 * @author Kiko
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPicker(
    dialogVisibleState: MutableState<Boolean>,
    oldColorsClick: (genOptions: GeneralOptions, colorVal: Color) -> Unit,
    applyColorClick: (genOptions: GeneralOptions, colorVal: Color) -> Unit
) {
    val controller = rememberColorPickerController()
    val colorHex = remember { mutableStateOf("#FFFFFF") }
    val colorValue = remember { mutableStateOf(Color.White) }
    val genOptions = roomDb!!.optionsDao().get()

    if (dialogVisibleState.value) {
        AlertDialog(
            modifier = Modifier.dialogPos(DialogPosition.BOTTOM),
            properties = DialogProperties(
                dismissOnClickOutside = true,
                dismissOnBackPress = true
            ),
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = { dialogVisibleState.value = false },
            title = {
                RoundedCard(
                    textColor = MaterialTheme.colorScheme.onSurface,
                    text = "Выберите цвет",
                    textSize = 16.sp,
                    spacing = 0.sp
                )
            },
            tonalElevation = 0.dp,
            text = {
                Column {
                    HsvColorPicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.3f),
                        controller = controller,
                        onColorChanged = { colorEnvelope: ColorEnvelope ->
                            colorValue.value = colorEnvelope.color // ARGB color value.
                            colorHex.value =
                                colorEnvelope.hexCode // Color hex code, which represents color value.
                        }
                    )
                    BrightnessSlider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .height(35.dp),
                        controller = controller,
                        borderRadius = 20.dp
                    )
                    Row(
                        modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 0.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Column(
                            modifier = Modifier.width(66.dp)
                        ) {
                            Card(
                                shape = RoundedCornerShape(15.dp),
                                colors = CardDefaults.cardColors(containerColor = colorValue.value),
                                modifier = Modifier.size(50.dp)
                            ) {}
                            Text(
                                text = colorHex.value.substring(2),
                                modifier = Modifier.padding(0.dp, 5.dp, 5.dp, 0.dp)
                            )
                        }
                        LazyRow(
                            modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            item {
                                Card(
                                    shape = RoundedCornerShape(15.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.Black),
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clickable {
                                            oldColorsClick(genOptions, Color.Black)
                                        }
                                ) {
                                    Box(
                                        contentAlignment = Alignment.CenterStart,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        RoundedCard(
                                            textColor = Color.White,
                                            text = "OLED",
                                            textSize = 6.sp,
                                            spacing = 0.sp
                                        )
                                    }
                                }
                            }
                            item {
                                Card(
                                    shape = RoundedCornerShape(15.dp),
                                    colors = CardDefaults.cardColors(containerColor = materialColors.primary),
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clickable {
                                            oldColorsClick(genOptions, materialColors.primary)
                                        }
                                ) {
                                }
                            }
                            item {
                                Card(
                                    shape = RoundedCornerShape(15.dp),
                                    colors = CardDefaults.cardColors(containerColor = materialColors.background),
                                    border = BorderStroke(
                                        1.dp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    ),
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clickable {
                                            oldColorsClick(genOptions, materialColors.background)
                                        }
                                ) {
                                }
                            }
                            roomDb!!.optionsDao().get().let { genOptions ->
                                if (genOptions.OldColors != null) { // Проверка есть ли цвета
                                    items(genOptions.OldColors!!.asReversed()) { color ->
                                        Card(
                                            shape = RoundedCornerShape(15.dp),
                                            colors = CardDefaults.cardColors(containerColor = color),
                                            modifier = Modifier
                                                .size(50.dp)
                                                .clickable {
                                                    oldColorsClick(genOptions, color)
                                                }
                                        ) {}
                                    }
                                } else {
                                    item {
                                        RoundedCard(
                                            textColor = MaterialTheme.colorScheme.onSurface,
                                            text = "Цветов пока нет",
                                            spacing = 0.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        dialogVisibleState.value = false
                    },
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    )
                ) {
                    Text(
                        text = "Отмена",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            },
            icon = {},
            confirmButton = {
                OutlinedButton(
                    onClick = {
                        applyColorClick(genOptions, colorValue.value)
                    },

                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    )
                ) {
                    Text(
                        text = "Принять",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        )
    }
}

/**
 * Показать превью расписание, где написаны занятия и их время
 *
 * @author Kiko
 */
@Composable
fun ShowSchedulePreviewDialog(
    dialogVisibleState: MutableState<Boolean>,
    schedule: Schedule
) {

    if (dialogVisibleState.value) {
        StandardBottomSheet(dialogVisibleState) {
            Text(
                schedule.Name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.padding(bottom = 16.dp))

            ScheduleBand(schedule = schedule)

            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = {
                    curSchedule = schedule
                    prefs?.edit()?.putString("mainSchedule", curSchedule!!.ID)?.apply()
                    dialogVisibleState.value = false
                    navController.popBackStack()
                })
            {
                Text(
                    text = "Выбрать",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

