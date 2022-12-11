package com.kikoproject.uwidget.objects

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.kikoproject.uwidget.models.User

/**
 * Возвращает раскрашенный текст, по принципу %n и %s заменяет на имя и фамилию
 * раскрашивает вторичным цветом а обычный текст возвращает классическим цветом текста
 *
 * @param text текст на замену
 * @param user пользователь
 *
 * @return раскрашенная строка
 *
 * @author Kiko
 */
@Composable
fun String.colorize(): AnnotatedString {
    val textParts = this.count { it == '%' }

    val pr_array = mutableListOf<String>() // Цветной текст
    val sec_array = mutableListOf<String>() // Обычный текст
    var inColorizing = false // Переменная для определения в какой массив записывать текущий символ
    val isFirstColorizing = this[0] == '%'
    var i = if (this[0] == '%') -1 else 0// Указывает на элемент в массивах для записи символа

    for (textPart: Int in 0..textParts - 1) { // Добавляем элементы в которые потом будут записываться символы
        pr_array.add(textPart, "")
        sec_array.add(textPart, "")
    }

    for (symbol: Char in this) { // Перебираем символы
        if (symbol == '%') { // Если видим символ окраски то инвертируем переменную окраски
            inColorizing = !inColorizing
            i++
            continue
        }

        if (!inColorizing) {
            sec_array[i] = sec_array[i] + symbol
        } else {
            pr_array[i] = pr_array[i] + symbol
        }
    }

    var returnString = buildAnnotatedString {}

    sec_array.forEachIndexed { index, _ ->
        if(isFirstColorizing) { // Если первым будет окрашаевымый то используем первый если нет то второй
            // TODO("Фиксануть это и найти способ другой")
            if (index % 2 == 0) {
                returnString += buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.primary
                        )
                    ) {
                        append(pr_array[index])
                    }
                }
            } else {
                returnString += buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.surface
                        )
                    ) {
                        append(sec_array[index])
                    }
                }
            }
        }
        else{
            if (index % 2 != 0) {
                returnString += buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.primary
                        )
                    ) {
                        append(pr_array[index])
                    }
                }
            } else {
                returnString += buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.surface
                        )
                    ) {
                        append(sec_array[index])
                    }
                }
            }
        }
    }

    return returnString
}