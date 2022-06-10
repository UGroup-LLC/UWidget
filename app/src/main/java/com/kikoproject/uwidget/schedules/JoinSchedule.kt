package com.kikoproject.uwidget.schedules

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.kikoproject.uwidget.main.navController
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.networking.EnterInScheduleResult
import com.kikoproject.uwidget.networking.enterInSchedule
import com.kikoproject.uwidget.objects.BackHeader
import com.kikoproject.uwidget.objects.CustomToastBar
import com.kikoproject.uwidget.objects.customToastBarMessage
import com.radusalagean.infobarcompose.InfoBar

@Composable
fun JoinSchedule() {
    // Штука которая будет нам показывать что мы чето не правильно сделали и тд
    var message: CustomToastBar? by remember { mutableStateOf(null) }
//    var chek = 0

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
                    val isError = remember { mutableStateOf(false) }
                    val code = joinCodeInput(isError)
                    if (isComplited.value){
                        /*if (chek == 3){
                            ShowInfoDialog(text = "Ошибка", {isComplited.value = false})
                        }*/
                        ShowInfoDialog(text = "Ошибка", textInfo = "Ок", {isComplited.value = false})
                    }
                    if (code.value.length == 6 && !isComplited.value) {
                        // Проверка есть ли такое расписание с таким кодом, состоим ли мы уже в нем или админ ли мы в нем
                        enterInSchedule(code.value, object : EnterInScheduleResult {
                            override fun onResult(isEntered: Boolean) {
                                if (isEntered) {
                                    isError.value = false
                                    navController.navigate(ScreenNav.Dashboard.route)
                                } else {
                                    isComplited.value = true
                                    isError.value = true
//                                    chek += 1
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

@OptIn(ExperimentalAnimationApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
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
    if (isError.value) {
        textFieldValues.forEach { field ->
            field.value = TextFieldValue("")
            focusRequester[0].requestFocus()
        }
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
                    if (it.text.length <= 1) {
                        textFieldValue.value = it
                        if (index + 1 < focusRequester.size && it.text.length == 1)
                            focusRequester[index + 1].requestFocus()
                        else if (index - 1 != -1 && it.text.length == 0) {
                            isError.value = false
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
                    .focusRequester(focusRequester[index])
                    .onFocusChanged {
                        isError.value = false
                    },
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
    return returningValue
}