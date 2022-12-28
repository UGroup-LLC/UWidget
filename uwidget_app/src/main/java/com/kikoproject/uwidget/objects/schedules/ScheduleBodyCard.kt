package com.kikoproject.uwidget.objects.schedules

import androidx.compose.runtime.Composable
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.time.TimeZone

@Composable
fun ScheduleBodyCard(schedule: Schedule, timeZone: TimeZone){
    when(timeZone)
    {
        TimeZone.MORNING -> {
            ScheduleMorningCard()
        }
        TimeZone.EVENING -> {
            ScheduleEveningCard()
        }
        TimeZone.DAY_LESION -> {
            ScheduleLesionCard()
        }
        TimeZone.DAY_REST -> {
            ScheduleRestCard()
        }
        else -> {

        }
    }
}