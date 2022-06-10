package com.kikoproject.uwidget.dialogs

import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.content.Context
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kikoproject.uwidget.ScheduleGetterSelectors
import com.kikoproject.uwidget.getSelectors
import com.kikoproject.uwidget.main.navController
import com.kikoproject.uwidget.navigation.ScreenNav

@Composable
fun ShowLoadingDialog(state: MutableState<Boolean>) {
    val textColor = MaterialTheme.colors.surface
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

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ShowSearchSelector(state: MutableState<Boolean>, url: String, result: ScheduleDialogSelector) {
    val textColor = MaterialTheme.colors.surface
    var found = remember { mutableStateOf(Pair(mutableListOf<String>(), mutableListOf<String>())) }
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
    /* AlertDialog(
         properties = DialogProperties(dismissOnClickOutside = false),
         containerColor = MaterialTheme.colors.background,
         onDismissRequest = { state.value = false },
         title = {
             Text(
                 text = "Поиск по сайту",
                 textAlign = TextAlign.Center,
                 color = textColor
             )
         },
         text = {
             LazyColumn(
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(15.dp),
                 horizontalAlignment = Alignment.CenterHorizontally
             ) {
                 item {
                     FastOutlineTextField(
                         text = "1 пара 1 день",
                         state = textFieldState,
                         textColor = textColor
                     )
                 }
             }
         },
         dismissButton = {

         },
         icon = {
             Icon(
                 imageVector = Icons.Rounded.Search,
                 contentDescription = null,
                 tint = MaterialTheme.colors.primary
             )
         },
         confirmButton = {
             Column(
                 modifier = Modifier.fillMaxWidth(),
                 horizontalAlignment = Alignment.CenterHorizontally
             ) {
                 Button(onClick = {
                     loadingState.value = true
                     getSelectors(
                         scope,
                         url,
                         textFieldState.value.text,
                         object : ScheduleGetterSelectors {
                             override fun onResult(schedules: Pair<MutableList<String>, MutableList<String>>) {
                                 found.value = schedules
                                 loadingState.value = false
                                 foundState.value = true
                             }
                         })
                 }) {
                     Text(text = "Найти", color = MaterialTheme.colors.secondary)
                 }
             }
         })

     */
}


@Composable
fun ShowFoundResult(
    state: MutableState<Boolean>,
    foundCollection: Pair<List<String>, List<String>>,
    result: ScheduleDialogSelector
) {
    val textColor = MaterialTheme.colors.surface
    var returned = ""
    if (state.value) {
        AlertDialog(
            properties = DialogProperties(dismissOnClickOutside = false),
            containerColor = MaterialTheme.colors.background,
            onDismissRequest = { state.value = false },
            title = {
                Text(
                    text = "Надены совпадения",
                    textAlign = TextAlign.Center,
                    color = textColor
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
                                containerColor = MaterialTheme.colors.primary
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(text = foundItem, color = MaterialTheme.colors.secondary)
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
                    tint = MaterialTheme.colors.primary
                )
            },
            confirmButton = {
            })
    }
}


@Composable
fun ShowErrorDialog(text: String, needButton: Boolean) {
    val state = remember { mutableStateOf(true) }
    val textColor = MaterialTheme.colors.surface
    if (state.value) {
        AlertDialog(
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            ),
            containerColor = MaterialTheme.colors.background,
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
                            Text(text = text, color = MaterialTheme.colors.secondary)
                        }
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

@Composable
fun ShowInfoDialog(text: String, textInfo: String, content:() -> Unit) {
    val state = remember { mutableStateOf(true) }
    val textColor = MaterialTheme.colors.surface
    if (state.value) {
        AlertDialog(
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            ),
            containerColor = MaterialTheme.colors.background,
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
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Button(onClick = {
                        content()
                    }) {
                        Text(text = textInfo, color = MaterialTheme.colors.secondary)
                    }
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
            },
            confirmButton = {

            })
    }
}

interface ScheduleDialogSelector {
    fun onResult(scheduleCSS: String)
}

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