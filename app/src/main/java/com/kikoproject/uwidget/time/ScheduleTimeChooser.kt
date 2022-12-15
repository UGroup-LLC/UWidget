package com.kikoproject.uwidget.time

import android.annotation.SuppressLint
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.utils.toLocalTimeRange
import com.kikoproject.uwidget.utils.toTimeRange
import java.time.LocalTime

/**
 * Временные зоны
 * @author Kiko
 */
enum class TimeZone {
    MORNING, DAY_LESION, DAY_REST, EVENING, ERROR
}

/**
 * Получает текущюю временную зону
 * @return возвращает временную зону расписания
 * @author Kiko
 */
@SuppressLint("SimpleDateFormat")
fun Schedule.getTimeZone(): TimeZone {
    val date = LocalTime.now()

    val sTimeM = LocalTime.parse(this.Time[0].toTimeRange()?.start ?: return TimeZone.ERROR)
    val sTimeE = LocalTime.parse(this.Time.last().toTimeRange()?.start ?: return TimeZone.ERROR)

    this.Time.forEachIndexed { _, time ->
        if (date in (time.toTimeRange()?.toLocalTimeRange() ?: return TimeZone.ERROR)) {
            return TimeZone.DAY_LESION
        }
    }

    return when (true) {
        (sTimeM > date) -> TimeZone.MORNING
        (sTimeE < date) -> TimeZone.EVENING
        else -> TimeZone.DAY_REST
    }
}