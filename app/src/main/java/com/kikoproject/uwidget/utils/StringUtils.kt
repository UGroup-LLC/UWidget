package com.kikoproject.uwidget.utils

import java.time.LocalTime

/**
 * Возращает range времени из строки
 *
 * @author Kiko
 */
fun String.toTimeRange(): ClosedRange<String> {
    try {
        return this.split("..")[0]..this.split("..")[1]
    } catch (exception: Exception) {
        return ""..""
    }
}

/**
 * Возращает range времени из range строки
 *
 * @author Kiko
 */
fun ClosedRange<String>.toLocalTimeRange(): ClosedRange<LocalTime> {
    return this.start.toLocalTime()..this.endInclusive.toLocalTime()
}

/**
 * Возращает LocalTime из строки
 */
fun String.toLocalTime(): LocalTime {
    try {
        return LocalTime.of(this.split(":")[0].toInt(), this.split(":")[1].toInt())
    } catch (exception: Exception) {
        return LocalTime.MIN
    }
}