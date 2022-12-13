package com.kikoproject.uwidget.time

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.utils.toLocalTimeRange
import com.kikoproject.uwidget.utils.toTimeRange
import java.time.LocalTime
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
 * @return возвращает временную зону расписания
 * @author Kiko
 */
@SuppressLint("SimpleDateFormat")
fun Schedule.getTimeZone(): TimeZone {
    val date = LocalTime.now()

    val sTimeM = LocalTime.parse(this.Time[0].toTimeRange().start)
    val sTimeE = LocalTime.parse(this.Time.last().toTimeRange().start)

    this.Time.forEachIndexed { index, time ->
        if(date in time.toTimeRange().toLocalTimeRange()){
            return TimeZone.DAY_LESION
        }
    }

    if (sTimeM > date) {
        return TimeZone.MORNING
    } else if(sTimeE < date) {
        return TimeZone.EVENING
    }

    return TimeZone.DAY_REST
}