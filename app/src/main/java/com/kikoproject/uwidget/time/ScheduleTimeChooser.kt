package com.kikoproject.uwidget.time

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import com.kikoproject.uwidget.models.schedules.Schedule
import java.util.*

/**
 * Временные зоны
 * @author Kiko
 */
enum class TimeZone {
    MORNING, DAY_LESION, DAY_REST, EVENING
}

/**
 * Получает текущюю временную зону
 * @param schedule расписание
 * @return возвращает временную зону расписания
 * @author Kiko
 */
@SuppressLint("SimpleDateFormat")
fun getTimeZone(schedule: Schedule): TimeZone {
    val date =
        SimpleDateFormat("HH:mm").parse(
            Calendar.getInstance().time.hours.toString() + ":"
                    + Calendar.getInstance().time.minutes.toString()
        )

    val sTimeM = SimpleDateFormat("HH:mm").parse(schedule.Time[0])
    val sTimeE = SimpleDateFormat("HH:mm").parse(schedule.Time.last())
    return if (sTimeM > date) {
        TimeZone.MORNING
    } else if (sTimeM < date && sTimeE > date) {
        TimeZone.DAY_LESION
    } else {
        TimeZone.EVENING
    }
}