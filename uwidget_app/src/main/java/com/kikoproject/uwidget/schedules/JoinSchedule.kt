package com.kikoproject.uwidget.schedules

import android.os.CountDownTimer
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.kikoproject.uwidget.main.*
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.networking.EnterInScheduleResult
import com.kikoproject.uwidget.networking.enterInSchedule
import com.kikoproject.uwidget.objects.BackHeader
import com.kikoproject.uwidget.objects.camera.JoinQRCodeScanner


/**
 * Окно присоединения к расписанию содержащяя в себе 6 полей для ввода кода
 * @author Kiko, Levosllavny
 */


@Composable
fun JoinSchedule() {
    // Штука которая будет нам показывать что мы чето не правильно сделали и тд
    //var message: CustomToastBar? by remember { mutableStateOf(null) }
    var code = remember { mutableStateOf("") }
    val isQrUsing = remember { mutableStateOf(false) }
    val isQrError = remember { mutableStateOf(false) }
    val isError = remember { mutableStateOf(false) }

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


        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 15.dp, vertical = 10.dp)
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
                            text = "Временно ввод кода расписаний недоступен"
                        )
                        Text(
                            text = "Времени до снятия бана: ${timeUntilBanIslifted.value}"
                        )

                    }
                }
            }
        }
    } else {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 15.dp, vertical = 10.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                BackHeader("Код приглашения")
                Box(contentAlignment = Alignment.TopCenter) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        Text(
                            text = "Введите код приглашения ниже"
                        )
                        val isComplited = remember { mutableStateOf(false) }

                        code = joinCodeInput(isError, isQrUsing)

                        if (isComplited.value) {
                            when (countOfBan) {
                                4 -> {
                                    ShowInfoDialog(
                                        text = "Будьте внимательны, это последняя попытка",
                                        buttonText = "Ок"
                                    ) { isComplited.value = false }

                                }
                                5 -> {
                                    isJoinBanned = true
                                }
                                else -> {
                                    isComplited.value = false
                                }
                            }
                        }
                        if (code.value.length == 6 && !isComplited.value) {
                            // Проверка есть ли такое расписание с таким кодом, состоим ли мы уже в нем или админ ли мы в нем
                            enterInSchedule(code.value, object : EnterInScheduleResult {
                                override fun onResult(isEntered: Boolean) {
                                    if(!isQrUsing.value) {
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
                                    else{
                                        if (isEntered) {
                                            isQrError.value = false
                                            navController.navigate(ScreenNav.Dashboard.route)
                                            countOfBan = 0
                                        } else {
                                            isComplited.value = true
                                            isQrError.value = true
                                            countOfBan += 1
                                            code.value = ""
                                        }
                                    }
                                }
                            })
                        }
                        else if(code.value.length != 6){
                            isQrError.value = true
                        }
                    }
                }
                JoinQRCodeScanner(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(256.dp)
                        .border(
                            2.dp,
                            color = if(isQrUsing.value && isQrError.value) {
                                MaterialTheme.colorScheme.error
                            }
                            else{
                                MaterialTheme.colorScheme.primary
                            },
                            RoundedCornerShape(16.dp)
                        )
                        .clip(
                            RoundedCornerShape(16.dp)
                        ),
                    isQrCode = isQrUsing,
                    qrCodeValue = code
                )
            }
        }
    }
}

/**
 * 6 полей для ввода кода (сами поля)
 * @author Kiko
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun joinCodeInput(
    isError: MutableState<Boolean>,
    isQrError: MutableState<Boolean>
): MutableState<String> {
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
                    isQrError.value = false
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
                    textColor = MaterialTheme.colorScheme.onSurface
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