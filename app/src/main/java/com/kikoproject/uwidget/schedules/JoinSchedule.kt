package com.kikoproject.uwidget.schedules

import android.os.CountDownTimer
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.dialogs.ShowInfoDialog
import com.kikoproject.uwidget.main.countOfBan
import com.kikoproject.uwidget.main.isJoinBanned
import com.kikoproject.uwidget.main.timer
import com.kikoproject.uwidget.main.timeUntilBanIslifted
import com.kikoproject.uwidget.main.navController
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.networking.EnterInScheduleResult
import com.kikoproject.uwidget.networking.enterInSchedule
import com.kikoproject.uwidget.objects.BackHeader
import com.kikoproject.uwidget.objects.CustomToastBar


/**
 * Окно присоединения к расписанию содержащяя в себе 6 полей для ввода кода
 * @author Kiko, Levosllavny
 */


@Composable
fun JoinSchedule() {
    // Штука которая будет нам показывать что мы чето не правильно сделали и тд
    //var message: CustomToastBar? by remember { mutableStateOf(null) }

    if (isJoinBanned) {
        if (timer) {
            timer = false
            object : CountDownTimer(30000, 1000) {

                override fun onTick(second: Long) {
                    timeUntilBanIslifted.value = second / 1000
                }

                override fun onFinish() {
                    isJoinBanned = false
                    countOfBan = 0
                    timer = true

                }
            }.start()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(horizontal = 15.dp, vertical = 10.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                BackHeader("Код приглашения")
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Временно ввод кода расписаний недоступен",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.surface
                        )
                        Text(
                            text = "Времени до снятия бана: ${timeUntilBanIslifted.value}",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.surface
                        )

                    }
                }
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(horizontal = 15.dp, vertical = 10.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                BackHeader("Код приглашения")
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Введите код приглашения ниже",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.surface
                        )
                        val isComplited = remember { mutableStateOf(false) }
                        val isError =remember {  mutableStateOf(false)}
                        val code = joinCodeInput(isError)
                        if (isComplited.value) {
                            if (countOfBan == 4) {
                                ShowInfoDialog(
                                    text = "Будьте внимательны, это последняя попытка",
                                    buttonText = "Ок",
                                    { isComplited.value = false })

                            } else if (countOfBan == 5) {
                                isJoinBanned = true
                            } else {
                                isComplited.value = false
                            }
                        }
                        if (code.value.length == 6 && !isComplited.value) {
                            // Проверка есть ли такое расписание с таким кодом, состоим ли мы уже в нем или админ ли мы в нем
                            enterInSchedule(code.value, object : EnterInScheduleResult {
                                override fun onResult(isEntered: Boolean) {
                                    if (isEntered) {
                                        isError.value = false
                                        navController.navigate(ScreenNav.Dashboard.route)
                                        countOfBan = 0
                                    } else {
                                        isComplited.value = true
                                        isError.value = true
                                        countOfBan += 1
                                        code.value = ""
                                    }
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}

/**
 * 6 полей для ввода кода (сами поля)
 * @author Kiko
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun joinCodeInput(isError: MutableState<Boolean>): MutableState<String> {
    val returningValue = remember { mutableStateOf("") }
    val focusRequester = remember {
        mutableListOf(
            FocusRequester(),
            FocusRequester(),
            FocusRequester(),
            FocusRequester(),
            FocusRequester(),
            FocusRequester()
        )
    }
    val textFieldValues = remember {
        mutableListOf(
            mutableStateOf(
                TextFieldValue("")
            ),
            mutableStateOf(
                TextFieldValue("")
            ),
            mutableStateOf(
                TextFieldValue("")
            ),
            mutableStateOf(
                TextFieldValue("")
            ),
            mutableStateOf(
                TextFieldValue("")
            ),
            mutableStateOf(
                TextFieldValue("")
            )
        )
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(10.dp)
    ) {
        itemsIndexed(textFieldValues) { index, textFieldValue ->
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                value = textFieldValue.value,
                shape = RoundedCornerShape(10.dp),
                isError = isError.value,
                onValueChange = {
                    isError.value = false
                    if (it.text.length <= 1) {
                        textFieldValue.value = it
                        if (index + 1 < focusRequester.size && it.text.length == 1) {
                            focusRequester[index + 1].requestFocus()
                        }
                    }
                    returningValue.value = ""
                    textFieldValues.forEach { field ->
                        returningValue.value += field.value.text
                    }
                },
                modifier = Modifier
                    .onKeyEvent {
                        if (it.key.keyCode == Key.Backspace.keyCode) {
                            textFieldValue.value = TextFieldValue("")
                            if (index - 1 != -1) {
                                focusRequester[index - 1].requestFocus()
                            }
                        }
                        true
                    }
                    .width(42.dp)
                    .height(50.dp)
                    .focusRequester(focusRequester[index]),
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = Color.Transparent,
                    errorCursorColor = Color.Transparent,
                    textColor = MaterialTheme.colors.surface
                )
            )
        }
    }

    if (isError.value) {
        textFieldValues.forEach { field ->
            field.value = TextFieldValue("")
            focusRequester[0].requestFocus()
        }
    }
    return returningValue
}