package com.kikoproject.uwidget.objects.text

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

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
    val textParts = this.count { it == colorizeSymbol }
    if (textParts != 0) {
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

        for (symbol: Char in this) { // Перебираем символы
            if (symbol == colorizeSymbol) { // Если видим символ окраски то инвертируем переменную окраски
                inColorizing = !inColorizing
                i++
                continue
            }

            if (!inColorizing) {
                secondArray[i] = secondArray[i] + symbol
            } else {
                primaryArray[i] = primaryArray[i] + symbol
            }
        }

        var returnString = buildAnnotatedString {}

        secondArray.forEachIndexed { index, _ ->
            if (isFirstColorizing) { // Если первым будет окрашаевымый то используем первый если нет то второй
                // TODO("Фиксануть это и найти способ другой")
                if (index % 2 == 0) {
                    returnString += buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colors.primary
                            )
                        ) {
                            append(primaryArray[index])
                        }
                    }
                } else {
                    returnString += buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colors.surface
                            )
                        ) {
                            append(secondArray[index])
                        }
                    }
                }
            } else {
                if (index % 2 != 0) {
                    returnString += buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colors.primary
                            )
                        ) {
                            append(primaryArray[index])
                        }
                    }
                } else {
                    returnString += buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colors.surface
                            )
                        ) {
                            append(secondArray[index])
                        }
                    }
                }
            }
        }

        return returnString
    }
    return buildAnnotatedString {}
}