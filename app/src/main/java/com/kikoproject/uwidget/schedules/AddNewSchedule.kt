package com.kikoproject.uwidget.schedules

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.*
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.dialogs.ScheduleDialogSelector
import com.kikoproject.uwidget.dialogs.ShowSearchSelector
import com.kikoproject.uwidget.dialogs.WebPageScreen
import com.kikoproject.uwidget.objects.ExpandableTextHelper
import com.kikoproject.uwidget.objects.IncreaseButtons
import com.kikoproject.uwidget.objects.ScheduleCardCreator
import com.kikoproject.uwidget.ui.theme.themeTextColor

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSchedule() {
    val textColor = themeTextColor()
    val nameState = remember { mutableStateOf(TextFieldValue(text = "")) }
    val categoryState = remember { mutableStateOf(TextFieldValue(text = "")) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = "Создание расписания",
                    fontFamily = FontFamily(Font(R.font.gogh)),
                    color = textColor,
                    fontSize = 24.sp
                )



                Divider(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(0.6f), color = textColor.copy(alpha = 0.4f)
                )

                OutlinedTextField(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .fillMaxWidth(0.8f),
                    value = nameState.value,
                    onValueChange = { nameState.value = it },
                    singleLine = true,
                    label = {
                        Text(
                            text = "Введите имя расписания",
                            color = textColor.copy(alpha = 0.4f)
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    textStyle = TextStyle(color = textColor)
                )

                OutlinedTextField(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .fillMaxWidth(0.8f),
                    value = categoryState.value,
                    onValueChange = { categoryState.value = it },
                    singleLine = true,
                    label = {
                        Text(
                            text = "Введите имя категории",
                            color = textColor.copy(alpha = 0.4f)
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    textStyle = TextStyle(color = textColor)
                )

                ExpandableTextHelper(
                    cardColor = textColor.copy(alpha = 0.2f),
                    titleSize = 12.sp,
                    titleColor = textColor.copy(alpha = 0.25f),
                    fontSize = 14.sp,
                    title = "Что такое категория?",
                    text = "Если у вас множество расписаний, вы можете сгруппировать их в одну категорию (Например: Колледж моды - 1 курс), если вам не нужна категория оставьте поле пустым"
                )
                val primaryColor = MaterialTheme.colors.primary

                Text(
                    text = "Видимость расписания",
                    color = textColor,
                    modifier = Modifier.padding(top = 20.dp, bottom = 10.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                val scheduleAccessMode = IncreaseButtons(
                    texts = listOf("Локальное", "По ссылке", "Публичное"),
                    roundStrength = 30f,
                    inactiveColor = textColor.copy(0.1f),
                    activeColor = primaryColor.copy(0.5f),
                    fontSize = 12.sp
                )

                Text(
                    text = "Способ получения расписания",
                    color = textColor,
                    modifier = Modifier.padding(top = 20.dp, bottom = 10.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                val scheduleGetMode = IncreaseButtons(
                    texts = listOf("Ручное заполнение", "Получение с сайта"),
                    inactiveColor = textColor.copy(0.1f),
                    roundStrength = 30f,
                    activeColor = primaryColor.copy(0.5f),
                    fontSize = 11.5.sp
                )
                val days = listOf(
                    "Понедельник",
                    "Вторник",
                    "Среда",
                    "Четверг",
                    "Пятница",
                    "Суббота",
                    "Воскресеьне"
                )

                val max = remember { mutableStateOf(0) }
                val count = remember { mutableListOf<MutableState<Int>>() }

                Spacer(modifier = Modifier.padding(10.dp))

                if (scheduleGetMode == 0) {
                    Card(
                        modifier = Modifier
                            .padding(10.dp),
                        border = BorderStroke(
                            1.dp,
                            color = textColor.copy(0.2f)
                        ),
                        shape = RoundedCornerShape(25.dp),
                        containerColor = textColor.copy(alpha = 0.03f)
                    ) {

                        Box(
                            Modifier
                                .padding(top = 20.dp, bottom = 10.dp)
                                .fillMaxWidth(0.86f),
                            contentAlignment = Alignment.Center
                        ) {
                            ExpandableTextHelper(
                                cardColor = textColor.copy(alpha = 0.2f),
                                titleSize = 12.sp,
                                titleColor = textColor.copy(alpha = 0.25f),
                                fontSize = 14.sp,
                                title = "Как заполнить расписание?",
                                text = "Заполните ваше расписание на каждый день, если у вас есть окна между парами/уроками поставьте пробел для создания доп. поля. Поля будут создаваться бесконечно, поэтому последнее поле всегда будет пустым."
                            )
                        }
                        count.clear()

                        days.forEach { day ->
                            count.add(ScheduleCardCreator(titleText = day))
                        }

                        var tempMax = 0
                        count.forEach { cout ->
                            if (tempMax < cout.value - 2) {
                                tempMax = cout.value - 2
                            }
                        }
                        max.value = tempMax
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .padding(10.dp),
                        border = BorderStroke(
                            1.dp,
                            color = textColor.copy(0.2f)
                        ),
                        shape = RoundedCornerShape(25.dp),
                        containerColor = textColor.copy(alpha = 0.03f)
                    ) {

                        Box(
                            Modifier
                                .padding(top = 20.dp, bottom = 10.dp)
                                .fillMaxWidth(0.86f),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                ExpandableTextHelper(
                                    cardColor = textColor.copy(alpha = 0.2f),
                                    titleSize = 12.sp,
                                    titleColor = textColor.copy(alpha = 0.25f),
                                    fontSize = 14.sp,
                                    title = "Как настроить парсер?",
                                    text = "Если у вашего завдения имеется расписание, вы можете попробовать вытащить расписания с сайта заведения. Для этого вам необходимо на ПК в барузере нажать на текст расписания ПКМ, Исследовать элемент, ПКМ в DevTools на поле с текстом, Copy -> Selector"
                                )

                                val urlState =
                                    remember { mutableStateOf(TextFieldValue("")) }
                                val pairsState = remember { mutableStateOf(TextFieldValue("")) }
                                val sluState =
                                    remember { mutableStateOf(TextFieldValue("")) }
                                val sldState =
                                    remember { mutableStateOf(TextFieldValue("")) }
                                val sruState =
                                    remember { mutableStateOf(TextFieldValue("")) }
                                FastOutlineTextField("Сайт для парсинга", urlState, textColor)
                                if(urlState.value.text.contains(".")) {
                                    FastOutlineTextField(
                                        "Сколько пар/уроков на сайте в день",
                                        pairsState,
                                        textColor
                                    )
                                }
                                if(urlState.value.text.contains(".") && pairsState.value.text != "") {
                                    FastOutlineTextField(
                                        "Понедельник - 1 пара",
                                        sluState,
                                        textColor,
                                        Icons.Rounded.Search,
                                        urlState.value.text
                                    )
                                    FastOutlineTextField(
                                        "Понедельник - 2 пара",
                                        sldState,
                                        textColor,
                                        Icons.Rounded.Search,
                                        urlState.value.text
                                    )
                                    FastOutlineTextField(
                                        "Вторник - 1 пара",
                                        sruState,
                                        textColor,
                                        Icons.Rounded.Search,
                                        urlState.value.text
                                    )
                                }

                                val schedulesTemp =
                                    remember { mutableStateOf(mutableListOf<Pair<Int, String>>()) }


                                schedulesTemp.value.forEach {
                                    Text(
                                        text = "День: ${it.first}, Пара: ${it.second}",
                                        color = textColor,
                                        modifier = Modifier.padding(horizontal = 20.dp)
                                    )
                                }
                                val scope = rememberCoroutineScope()
                                val scopeSchedule = rememberCoroutineScope()

                                Button(
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                        .fillMaxWidth(0.9f),
                                    onClick = {

                                        val firstDivider = getSelectorDivider(
                                            sluState.value.text,
                                            sldState.value.text
                                        )
                                        val secondDivider = getSelectorDivider(
                                            sluState.value.text,
                                            sruState.value.text
                                        )

                                        getSchedule(
                                            scope,
                                            url = urlState.value.text,
                                            sluState.value.text,
                                            firstDivider,
                                            secondDivider,
                                            pairsState.value.text.toInt(),
                                            object : ScheduleGetter {
                                                override fun onResult(schedules: MutableList<Pair<Int, String>>) {
                                                    schedulesTemp.value = schedules
                                                }
                                            }
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colors.primary.copy(
                                            alpha = 0.5f
                                        )
                                    ),
                                ) {
                                    Text(
                                        text = "Пропарсить сайт",
                                        color = textColor,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                if (max.value >= 0) {
                    ScheduleCardCreator(cardsInt = max.value, titleText = "Время")
                }
            }
        }
    }
}

@Composable
fun FastOutlineTextField(
    text: String,
    state: MutableState<TextFieldValue>,
    textColor: Color,
) {
    OutlinedTextField(
        modifier = Modifier.padding(
            bottom = 10.dp,
            start = 10.dp,
            end = 10.dp
        ),
        value = state.value,
        onValueChange = {
            state.value = it
        },
        label = {
            Text(
                text = text,
                color = textColor.copy(alpha = 0.4f)
            )
        },
        shape = RoundedCornerShape(16.dp),
        textStyle = TextStyle(color = textColor)
    )
}

@Composable
fun FastOutlineTextField(
    text: String,
    state: MutableState<TextFieldValue>,
    textColor: Color,
    imageVector: ImageVector? = null,
    url: String
) {
    val dialogState = remember {
        mutableStateOf(false)
    }

    if (dialogState.value) {
        ShowSearchSelector(state = dialogState, url, object : ScheduleDialogSelector{
            override fun onResult(scheduleCSS: String) {
                state.value = TextFieldValue(scheduleCSS)
            }

        })
    }

    OutlinedTextField(
        modifier = Modifier.padding(
            bottom = 10.dp,
            start = 10.dp,
            end = 10.dp
        ),
        value = state.value,
        onValueChange = {
            state.value = it
        },
        leadingIcon = {
            if (imageVector != null) {
                IconButton(
                    onClick = { dialogState.value = true },
                ) {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = "Search",
                        tint = MaterialTheme.colors.primary,
                    )
                }
            }
        },
        label = {
            Text(
                text = text,
                color = textColor.copy(alpha = 0.4f)
            )
        },
        shape = RoundedCornerShape(16.dp),
        textStyle = TextStyle(color = textColor)
    )
}

@Preview(showBackground = true)
@Composable
fun AddSchedulePreview() {
    AddSchedule()
}
