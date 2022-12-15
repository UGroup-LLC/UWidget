package com.kikoproject.uwidget.utils

import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.time.TimeZone
import com.kikoproject.uwidget.time.getTimeZone
import java.time.LocalTime
import java.util.*

/**
 * Возвращает самое ближайшее расписание
 *
 * @author Kiko
 */
fun Schedule.getClosestLesion(indexPlus: Int = 0): String? {
    this.Time.toTimeRange()?.let { timeRange ->
        val day =
            (Calendar.getInstance() as GregorianCalendar).toZonedDateTime().dayOfWeek.ordinal

        val closestTime = if (getTimeZone() == TimeZone.DAY_LESION) {
            LocalTime.now().getCloseTimeRange(timeRange) // Если идет урок
        }
        else
        {
            LocalTime.now().getCloseTimeBetweenRange(timeRange)?.fromBetweenToLesionTime(timeRange) // Если идет перемена
        }

        if (closestTime != null) {
            val index =
                this.Time.toTimeRange()?.indexOf(closestTime)
            if (index != null) {
                if (this.Time.getOrNull(index + indexPlus) != null) {
                    return this.Schedule[day.toString()]?.getOrNull(index + indexPlus)
                }
            }
        }
    }
    return null
}