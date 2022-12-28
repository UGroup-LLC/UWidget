package com.kikoproject.uwidget.objects.cards

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import com.kikoproject.uwidget.dialogs.OpenTimePicker
import com.kikoproject.uwidget.dialogs.TimePickerResult
import java.time.LocalTime

class TimeCard {
    var resTime = mutableListOf(LocalTime.MIN..LocalTime.MAX)

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
    fun timeCardCreator(

        cardsInt: Int,

        //card options
        cardColor: Color = MaterialTheme.colorScheme.onSurface,
        cardShapeRadius: Dp = 20.dp,
        cardBorderSize: Dp = 1.dp,

        //title options
        titleText: String,
        titleFontSize: TextUnit = 14.sp,
        titleFontWeight: FontWeight = FontWeight.Medium,
        titleColor: Color = MaterialTheme.colorScheme.onSurface,

        //divider options
        dividerColor: Color = MaterialTheme.colorScheme.onSurface,
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
                        Text(
                            modifier = Modifier.padding(0.dp,8.dp,0.dp,0.dp),
                            text = "Занятие ${cardIndex+1}",
                            fontSize = titleFontSize,
                            fontWeight = titleFontWeight,
                            color = titleColor.copy(0.4f)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(7.dp)
                        ) {
                            resTime.add(
                                calendarButtons(
                                    context = context,
                                )
                            )
                        }
                    }
                }
            }
        }
        return states
    }

    /**
     * Карточки с первоначальной иконкой календаря, а в последующем заменяемые на выбранное из диалога
     * @author Kiko
     */
    @Composable
    fun calendarButtons(
        context: Context,

    ): ClosedRange<LocalTime> {
        /*
    Выбор времени начала урока и конца урока
     */
        val returnTimeRange = remember { mutableStateOf(LocalTime.MIN..LocalTime.MAX) }

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


        OpenTimePicker(
            context = context,
            timePickerResult = object : TimePickerResult {
                override fun onResult(time: LocalTime) {
                    timeFromDialog[0].value = time
                    timeStateVisible[0].value = false
                    returnTimeRange.value = time..returnTimeRange.value.endInclusive
                }

                override fun onClose() {
                    timeStateVisible[0].value = false
                    timeStateVisible[1].value = false
                }
            })

        OpenTimePicker(
            context = context,
            timePickerResult = object : TimePickerResult {
                override fun onResult(time: LocalTime) {
                    timeFromDialog[1].value = time
                    timeStateVisible[1].value = false
                    returnTimeRange.value = returnTimeRange.value.start..time
                }

                override fun onClose() {
                    timeStateVisible[0].value = false
                    timeStateVisible[1].value = false
                }
            })

        return returnTimeRange.value
    }

}