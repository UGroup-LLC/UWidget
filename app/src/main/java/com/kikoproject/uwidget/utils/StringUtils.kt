package com.kikoproject.uwidget.utils

import java.time.LocalTime

/**
 * Возращает range времени из строки
 *
 * @author Kiko
 */
fun String.toTimeRange(): ClosedRange<String> {
    return this.split("..")[0]..this.split("..")[1]
}

/**
 * Конвертирует лист стринга в лист range local time
 *
 * @author Kiko
 */
fun List<String>.toTimeRange(): List<ClosedRange<LocalTime>>? {
    val returnMap = this.map {
        it.toTimeRange().toLocalTimeRange()
    }

    val nullTime = LocalTime.of(0, 0)

    return if (returnMap.contains(nullTime..nullTime))
    {
        null
    }
    else
    {
        returnMap
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
    return try {
        LocalTime.of(this.split(":")[0].toInt(), this.split(":")[1].toInt())
    } catch (exception: Exception) {
        LocalTime.MIN
    }
}