package com.kikoproject.uwidget.objects.text

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.kikoproject.uwidget.main.options
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.models.schedules.options.ScheduleOptions

/**
 * Возвращает раскрашенный текст
 * раскрашивает вторичным цветом а обычный текст возвращает классическим цветом текста
 * Обязательно необходимы закрывающие color splitters
 *
 * @param colorizeSymbol символ который будет отвечать за текст который будет отмечаться цветом
 *
 * @return раскрашенная строка
 *
 * @author Kiko
 */
@Composable
fun String.colorize(colorizeSymbol: Char = '@'): AnnotatedString {
    if (this.contains(colorizeSymbol)) {
        val textParts = this.count { it == colorizeSymbol }
        if (textParts != 0) {
            val (primaryArray, secondArray, isFirstColorizing) = colorizeLogic( // Логика
                colorizeSymbol,
                textParts
            )

            var returnString = buildAnnotatedString {}

            secondArray.forEachIndexed { index, _ ->
                if (isFirstColorizing) { // Если первым будет окрашаевымый то используем первый если нет то второй
                    returnString += buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = if (index % 2 == 0) MaterialTheme.colors.primary else MaterialTheme.colors.surface
                            )
                        ) {
                            append(if (index % 2 == 0) primaryArray[index] else secondArray[index])
                        }
                    }
                } else {
                    returnString += buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = if (index % 2 != 0) MaterialTheme.colors.primary else MaterialTheme.colors.surface
                            )
                        ) {
                            append(if (index % 2 != 0) primaryArray[index] else secondArray[index])
                        }
                    }
                }
            }

            return returnString
        }
    }
    return buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colors.surface
            )
        ) {
            append(this@colorize)
        }
    }
}

/**
 * Возвращает раскрашенный текст
 * раскрашивает вторичным цветом а обычный текст возвращает классическим цветом текста
 * Обязательно необходимы закрывающие color splitters
 *
 * @param colorizeSymbol символ который будет отвечать за текст который будет отмечаться цветом
 *
 * @return раскрашенная строка
 *
 * @author Kiko
 */
@Composable
fun String.colorizeWidgetText(colorizeSymbol: Char = '@', schedule: Schedule): String {
    val primaryColor = java.lang.String.format(
        "#%06X",
        (0xFFFFFF and (options?.generalSettings?.borderColor?.toArgb()
            ?: Color.White.toArgb()))
    )
    val surfaceColor = java.lang.String.format(
        "#%06X",
        0xFFFFFF and (options?.generalSettings?.textColor?.toArgb()
            ?: Color.White.toArgb())
    )

    if (this.contains(colorizeSymbol)) {
        val textParts = this.count { it == colorizeSymbol }
        if (textParts != 0) {
            val (primaryArray, secondArray, isFirstColorizing) = colorizeLogic( // Логика
                colorizeSymbol,
                textParts
            )

            var returnString = ""

            secondArray.forEachIndexed { index, _ ->
                returnString += if (isFirstColorizing) { // Если первым будет окрашаевымый то используем первый если нет то второй
                    "<font color='" +
                            "${if (index % 2 == 0) primaryColor else surfaceColor}'>" +
                            "${if (index % 2 == 0) primaryArray[index] else secondArray[index]}</font>"
                } else {
                    "<font color='" +
                            "${if (index % 2 != 0) primaryColor else surfaceColor}'>" +
                            "${if (index % 2 != 0) primaryArray[index] else secondArray[index]}</font>"
                }
            }
            return returnString
        }
    }
    return "<font color='$surfaceColor'>${this}</font>"
}

@Composable
private fun String.colorizeLogic(
    colorizeSymbol: Char,
    textParts: Int
): Triple<MutableList<String>, MutableList<String>, Boolean> {
    val primaryArray = mutableListOf<String>() // Цветной текст
    val secondArray = mutableListOf<String>() // Обычный текст
    var inColorizing =
        false // Переменная для определения в какой массив записывать текущий символ
    val isFirstColorizing = this[0] == colorizeSymbol
    var i =
        if (this[0] == colorizeSymbol) -1 else 0// Указывает на элемент в массивах для записи символа

    for (textPart: Int in 0 until textParts) { // Добавляем элементы в которые потом будут записываться символы
        primaryArray.add(textPart, "")
        secondArray.add(textPart, "")
    }

    this.forEach { symbol -> // Перебираем символы
        if (symbol == colorizeSymbol) { // Если видим символ окраски то инвертируем переменную окраски
            inColorizing = !inColorizing
            i++
            return@forEach
        }

        if (!inColorizing) {
            secondArray[i] = secondArray[i] + symbol
        } else {
            primaryArray[i] = primaryArray[i] + symbol
        }
    }
    return Triple(primaryArray, secondArray, isFirstColorizing)
}