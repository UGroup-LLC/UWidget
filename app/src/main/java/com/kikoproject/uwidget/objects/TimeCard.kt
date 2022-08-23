package com.kikoproject.uwidget.objects

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.dialogs.TimePickerResult
import java.time.LocalTime

class TimeCard {
    public var resTime = mutableListOf(LocalTime.MIN..LocalTime.MAX)

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
                resTime.clear() // При обновлении карточки с временем мы очищаем массив с хранящимся массивом интервалом времен

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    for (cardIndex: Int in 0..cardsInt) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(7.dp)
                        ) {
                            if(cardIndex == 0) { // Если это первая карточка времени то мы ставим интервал от мин до макс
                                resTime.add(CalendarButtons(context = context, LocalTime.MIN..LocalTime.MAX))
                            }
                            else {
                                resTime.add(CalendarButtons(context = context, resTime[cardIndex-1].endInclusive..LocalTime.MAX))
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
     * @param cardIndex указывает на то какая эта карточка по индексу
     * @param timeRange указывает на то какой интервал времени доступен пользователю

     * @author Kiko
     */
    @Composable
    fun CalendarButtons(context: Context, timeRange: ClosedRange<LocalTime>) : ClosedRange<LocalTime>{
        /*
    Выбор времени начала урока и конца урока
     */
        val returnTimeRange = remember{ mutableStateOf(LocalTime.MIN..LocalTime.MAX) }

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
            com.kikoproject.uwidget.dialogs.OpenTimePicker(
                range = timeRange,
                context = context,
                titleText = "Выбор времени начала урока",
                timePickerResult = object : TimePickerResult {
                    override fun onResult(time: LocalTime) {
                        timeFromDialog[0].value = time
                        timeStateVisible[0].value = false
                        returnTimeRange.value = time..returnTimeRange.value.endInclusive
                    }

                    override fun onClose() {
                        timeStateVisible[0].value = false
                    }
                })
        }

        if (timeStateVisible[1].value) {
            com.kikoproject.uwidget.dialogs.OpenTimePicker(
                context = context,
                range = returnTimeRange.value,
                titleText = "Выбор времени конца урока",
                timePickerResult = object : TimePickerResult {
                    override fun onResult(time: LocalTime) {
                        timeFromDialog[1].value = time
                        timeStateVisible[1].value = false
                        returnTimeRange.value = returnTimeRange.value.start..time
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
                    timeFromDialog[0].value.toString(),
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
                    timeFromDialog[1].value.toString(),
                    color = MaterialTheme.colors.primary
                )
            }


        }
        return returnTimeRange.value
    }

}