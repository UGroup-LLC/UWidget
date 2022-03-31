package com.kikoproject.uwidget.schedules

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.models.SchedulesModel
import com.kikoproject.uwidget.objects.ExpandableTextHelper
import com.kikoproject.uwidget.objects.IncreaseButtons
import com.kikoproject.uwidget.objects.ScheduleCardCreator
import com.kikoproject.uwidget.ui.theme.themeTextColor

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

                val max = remember{ mutableStateOf(0) }
                val count = remember{mutableListOf<MutableState<Int>>()}

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
                        count.forEach{ cout ->
                            if(tempMax < cout.value-2){
                                tempMax = cout.value-2
                            }
                        }
                        max.value = tempMax
                    }
                }

                if(max.value >= 0) {
                    ScheduleCardCreator(cardsInt = max.value, titleText = "Время")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddSchedulePreview() {
    AddSchedule()
}