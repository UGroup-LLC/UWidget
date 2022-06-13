package com.kikoproject.uwidget.objects

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.dialogs.OpenTimePicker
import com.kikoproject.uwidget.dialogs.TimePickerResult
import java.time.LocalTime


/**
 * Карточка для заполнения расписания
 *
 * @param cardColor Задний цвет карточки
 * @param cardShapeRadius Закругление у карточки
 * @param cardBorderSize Размер контуров карточки
 *
 * @param titleText Текст заголовка
 * @param titleFontSize Шрифт заголовка
 * @param titleFontWeight Жирность шрифта
 * @param titleColor Цвет заголовка
 *
 * @param dividerColor Цвет разделителя
 *
 * @param textFieldColor Цвет полей ввода
 *
 * @author Kiko
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleCardCreator(

    //card options
    cardColor: Color = MaterialTheme.colors.surface,
    cardShapeRadius: Dp = 20.dp,
    cardBorderSize: Dp = 1.dp,

    //title options
    titleText: String,
    titleFontSize: TextUnit = 14.sp,
    titleFontWeight: FontWeight = FontWeight.Medium,
    titleColor: Color = MaterialTheme.colors.surface,

    //divider options
    dividerColor: Color = MaterialTheme.colors.surface,
    //textfield
    textFieldColor: Color = MaterialTheme.colors.surface
): MutableList<MutableState<TextFieldValue>> {
    val states = remember { mutableListOf(mutableStateOf(TextFieldValue(text = ""))) }
    val count = remember {
        mutableStateOf(1)
    }
    val textStates = remember { mutableListOf(mutableStateOf(true)) }

    Card(
        modifier = Modifier
            .padding(10.dp),
        border = BorderStroke(
            cardBorderSize,
            color = cardColor.copy(0.3f)
        ),
        shape = RoundedCornerShape(cardShapeRadius),
        colors = CardDefaults.cardColors(containerColor = cardColor.copy(alpha = 0.05f))
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                modifier = Modifier.padding(10.dp),
                text = titleText,
                fontSize = titleFontSize,
                fontWeight = titleFontWeight,
                color = titleColor.copy(0.4f)
            )

            Divider(
                color = dividerColor.copy(0.2f),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(bottom = 10.dp)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                states.forEachIndexed { index, state ->
                    OutlinedTextField(
                        modifier = Modifier.padding(bottom = 10.dp, start = 10.dp, end = 10.dp),
                        value = state.value,
                        onValueChange = {
                            state.value = it
                            if (it.text.isEmpty()) {
                                textStates[index].value = true
                                if (states.size > 1) {
                                    count.value -= 1
                                    states.removeAt(index)
                                }
                            } else {
                                if (textStates[index].value) {
                                    count.value += 1
                                    textStates[index].value = false
                                    textStates.add(mutableStateOf(true))
                                    states.add(mutableStateOf(TextFieldValue("")))
                                }
                            }
                        },
                        label = {
                            Text(
                                text = "Поле ${index + 1}",
                                color = textFieldColor.copy(alpha = 0.4f)
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        textStyle = TextStyle(color = textFieldColor)
                    )
                }
            }
        }
    }
    return states
}


/**
 * Карточка для заполнения расписания
 *
 * @param cardsInt Количество полей для ввода
 *
 * @param cardColor Задний цвет карточки
 * @param cardShapeRadius Закругление у карточки
 * @param cardBorderSize Размер контуров карточки
 *
 * @param titleText Текст заголовка
 * @param titleFontSize Шрифт заголовка
 * @param titleFontWeight Жирность шрифта
 * @param titleColor Цвет заголовка
 *
 * @param dividerColor Цвет разделителя
 *
 * @param textFieldColor Цвет полей ввода
 *
 * @author Kiko
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleCardCreator(

    cardsInt: Int,

    //card options
    cardColor: Color = MaterialTheme.colors.surface,
    cardShapeRadius: Dp = 20.dp,
    cardBorderSize: Dp = 1.dp,

    //title options
    titleText: String,
    titleFontSize: TextUnit = 14.sp,
    titleFontWeight: FontWeight = FontWeight.Medium,
    titleColor: Color = MaterialTheme.colors.surface,

    //divider options
    dividerColor: Color = MaterialTheme.colors.surface,

    //textfield
    textFieldColor: Color = MaterialTheme.colors.surface
): MutableList<MutableState<TextFieldValue>> {
    val states = mutableListOf<MutableState<TextFieldValue>>()

    for (cardIndex: Int in 0..cardsInt) {
        states.add(remember { mutableStateOf(TextFieldValue(text = "")) })
    }

    Card(
        modifier = Modifier
            .padding(10.dp),
        border = BorderStroke(
            cardBorderSize,
            color = cardColor.copy(0.3f)
        ),
        shape = RoundedCornerShape(cardShapeRadius),
        colors = CardDefaults.cardColors(containerColor = cardColor.copy(alpha = 0.05f))
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                modifier = Modifier.padding(10.dp),
                text = titleText,
                fontSize = titleFontSize,
                fontWeight = titleFontWeight,
                color = titleColor.copy(0.4f)
            )

            Divider(
                color = dividerColor.copy(0.2f),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(bottom = 10.dp)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                for (cardIndex: Int in 0..cardsInt) {
                    OutlinedTextField(
                        modifier = Modifier.padding(bottom = 10.dp, start = 10.dp, end = 10.dp),
                        value = states[cardIndex].value,
                        onValueChange = {
                            states[cardIndex].value = it
                        },
                        label = {
                            Text(
                                text = "Поле ${cardIndex + 1}",
                                color = textFieldColor.copy(alpha = 0.4f)
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        textStyle = TextStyle(color = textFieldColor)
                    )
                }
            }
        }
    }
    return states
}

/**
 * Карточка для заполнения времени в расписаниях
 *
 * @param cardsInt Количество полей для ввода времени
 *
 * @param cardColor Задний цвет карточки
 * @param cardShapeRadius Закругление у карточки
 * @param cardBorderSize Размер контуров карточки
 *
 * @param titleText Текст заголовка
 * @param titleFontSize Шрифт заголовка
 * @param titleFontWeight Жирность шрифта
 * @param titleColor Цвет заголовка
 *
 * @param dividerColor Цвет разделителя
 *
 * @author Kiko
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeCardCreator(

    cardsInt: Int,

    //card options
    cardColor: Color = MaterialTheme.colors.surface,
    cardShapeRadius: Dp = 20.dp,
    cardBorderSize: Dp = 1.dp,

    //title options
    titleText: String,
    titleFontSize: TextUnit = 14.sp,
    titleFontWeight: FontWeight = FontWeight.Medium,
    titleColor: Color = MaterialTheme.colors.surface,

    //divider options
    dividerColor: Color = MaterialTheme.colors.surface,
): MutableList<MutableState<TextFieldValue>> {
    val states = mutableListOf<MutableState<TextFieldValue>>()

    val context = LocalContext.current


    for (cardIndex: Int in 0..cardsInt) {
        states.add(remember { mutableStateOf(TextFieldValue(text = "")) })
    }

    Card(
        modifier = Modifier
            .padding(10.dp),
        border = BorderStroke(
            cardBorderSize,
            color = cardColor.copy(0.3f)
        ),
        shape = RoundedCornerShape(cardShapeRadius),
        colors = CardDefaults.cardColors(containerColor = cardColor.copy(alpha = 0.05f))
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            /*
            Заголовок
             */
            Text(
                modifier = Modifier.padding(10.dp),
                text = titleText,
                fontSize = titleFontSize,
                fontWeight = titleFontWeight,
                color = titleColor.copy(0.4f)
            )

            Divider(
                color = dividerColor.copy(0.2f),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(bottom = 10.dp)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                for (cardIndex: Int in 0..cardsInt) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(7.dp)
                    ) {
                        if (cardIndex == 0) {
                            val start = LocalTime.MIN..LocalTime.MAX
                            CalendarButtons(context = context, range = start)
                        }
                        else{

                        }
                    }
                }
            }
        }
    }
    return states
}

/**
 * Карточки с первоначальной иконкой календаря, а в последующем заменяемые на выбранное из диалога
 * @param range Диапазон мин времени и макс времени для выбора
 *
 * @author Kiko
 */
@Composable
fun CalendarButtons(context: Context, range: ClosedRange<LocalTime>) {
    /*
Выбор времени начала урока и конца урока
 */
    val timeFromDialog =
        remember {
            mutableListOf(
                mutableStateOf(LocalTime.MIN),
                mutableStateOf(LocalTime.MIN)
            )
        }

    val timeStateVisible = remember {
        mutableListOf(
            mutableStateOf(false),
            mutableStateOf(false)
        )
    }

    // Здесь идет открытие диалогов для 2-ух кнопок
    if (timeStateVisible[0].value) {
        OpenTimePicker(
            range = range,
            context = context,
            titleText = "Выбор времени начала урока",
            timePickerResult = object : TimePickerResult {
                override fun onResult(time: LocalTime) {
                    timeFromDialog[0].value = time
                    timeStateVisible[0].value = false
                }

                override fun onClose() {
                    timeStateVisible[0].value = false
                }
            })
    }

    if (timeStateVisible[1].value) {
        OpenTimePicker(
            context = context,
            range = range,
            titleText = "Выбор времени конца урока",
            timePickerResult = object : TimePickerResult {
                override fun onResult(time: LocalTime) {
                    timeFromDialog[1].value = time
                    timeStateVisible[1].value = false
                }

                override fun onClose() {
                    timeStateVisible[1].value = false
                }
            })
    }


    OutlinedButton(onClick = {
        timeStateVisible[0].value = true
    }) {

        if (timeFromDialog[0].value == LocalTime.MIN) {
            Icon(
                imageVector = Icons.Rounded.DateRange,
                contentDescription = null,
                tint = MaterialTheme.colors.primary
            )
        } else {
            Text(
                timeFromDialog[0].value.hour.toString() + ":" + timeFromDialog[0].value.minute.toString(),
                color = MaterialTheme.colors.primary
            )
        }
    }

    /*
    Разделитель
     */
    Divider(
        modifier = Modifier
            .width(20.dp)
            .padding(horizontal = 5.dp, vertical = 0.dp),
        thickness = 1.dp,
        color = MaterialTheme.colors.surface.copy(0.2f)
    )
    /*
    Втораяя кнопка
     */
    OutlinedButton(onClick = {
        timeStateVisible[1].value = true
    }) {

        if (timeFromDialog[1].value == LocalTime.MIN) {
            Icon(
                imageVector = Icons.Rounded.DateRange,
                contentDescription = null,
                tint = MaterialTheme.colors.primary
            )
        } else {
            Text(
                timeFromDialog[1].value.hour.toString() + ":" + timeFromDialog[1].value.minute.toString(),
                color = MaterialTheme.colors.primary
            )
        }


    }
}


@Preview
@Composable
fun PreviewCreator() {
    TimeCardCreator(3, titleText = "Понедельник")
}