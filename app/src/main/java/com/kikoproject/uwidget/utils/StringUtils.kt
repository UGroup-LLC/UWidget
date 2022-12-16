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

    return if (returnMap.contains(nullTime..nullTime)) {
        null
    } else {
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

fun List<String>.distinctTime(schedules: List<String>): List<ClosedRange<LocalTime>> {
    val timeRanges = this.toTimeRange()
    val returnTimeRange = mutableListOf<ClosedRange<LocalTime>>()

    var pastIndex = 0

    if (timeRanges != null) {
        schedules.forEachIndexed { index, _ ->
            if (schedules[pastIndex] != schedules[index] && index != 0) {
                returnTimeRange.add(timeRanges[pastIndex].start..timeRanges[index - 1].endInclusive)
                pastIndex = index
            }
        }
        if (schedules[pastIndex] == schedules[lastIndex]) { // Проверка если последние 2 пары будут одинаковые
            returnTimeRange.add(timeRanges[pastIndex].start..timeRanges[lastIndex].endInclusive)
        } else {
            returnTimeRange.add(timeRanges[schedules.lastIndex])
        }
    }
    return returnTimeRange
}

fun List<String>.distinctLesions(): List<String> {
    val returnList = mutableListOf<String>()

    var pastIndex = 0

    this.forEachIndexed { index, _ ->
        if (this[pastIndex] != this[index] && index != 0) {
            returnList.add(this[pastIndex])
            pastIndex = index
        }
    } // Проверка если последние 2 пары будут одинаковые
    returnList.add(this[lastIndex])
    return returnList
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