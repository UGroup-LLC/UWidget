package com.kikoproject.uwidget.utils

import com.kikoproject.uwidget.models.schedules.Schedule
import java.time.LocalTime
import java.util.*

/**
 * Возвращает самое ближайшее расписание
 *
 * @author Kiko
 */
fun Schedule.getClosestLesion(indexPlus: Int = 0): String?{
    this.Time.toTimeRange()?.let { timeRange ->
        val day =
            (Calendar.getInstance() as GregorianCalendar).toZonedDateTime().dayOfWeek.ordinal

        val closestTime = LocalTime.now().getCloseTimeRange(timeRange)
        if (closestTime != null) {
            val index =
                this.Time.toTimeRange()?.indexOf(closestTime)
            if (index != null) {
                return this.Schedule[day.toString()]?.getOrNull(index+indexPlus)
            }
        }
    }
    return null
}