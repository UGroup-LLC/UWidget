package com.kikoproject.uwidget.objects.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


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
fun scheduleCardCreator(

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
    //textfield
    textFieldColor: Color = MaterialTheme.colorScheme.onSurface
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
                                color = textFieldColor.copy(alpha = 0.4f),
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        textStyle = MaterialTheme.typography.labelMedium

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
fun scheduleCardCreator(

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

    //textfield
    textFieldColor: Color = MaterialTheme.colorScheme.onSurface
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
