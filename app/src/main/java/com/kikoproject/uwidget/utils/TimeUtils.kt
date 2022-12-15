package com.kikoproject.uwidget.utils

import java.time.LocalTime

/**
 * Эта функция проходится по массиву range времени и выдает в каком range времени находится время
 *
 * @return Возвращает range времени в котором находится this time
 * @author Kiko
 */
fun LocalTime.getCloseTimeRange(timeRangeArray: List<ClosedRange<LocalTime>>): ClosedRange<LocalTime>?{
    timeRangeArray.forEach { timeRange ->
        if(this in timeRange){
            return timeRange // Если мы нашли все четко возвращаем
        }
    }
    return null // Если нет такого то возвращаем null
}

/**
 * Эта функция проходится по массиву range времени и выдает в каком range between времени находится время
 *
 * @return Возвращает range времени в котором находится this time
 * @author Kiko
 */
fun LocalTime.getCloseTimeBetweenRange(timeRangeArray: List<ClosedRange<LocalTime>>): ClosedRange<LocalTime>?{
    val rebuildedTimeList = mutableListOf<ClosedRange<LocalTime>>()

    timeRangeArray.forEachIndexed { index, timeRange ->
        if(index+1 <= timeRangeArray.lastIndex) {
            rebuildedTimeList.add(timeRange.endInclusive..timeRangeArray[index + 1].start)
        }
    }

    rebuildedTimeList.forEach { timeRange ->
        if(this in timeRange){
            return timeRange // Если мы нашли все четко возвращаем
        }
    }
    return null // Если нет такого то возвращаем null
}