package com.kikoproject.uwidget.objects.schedules

import androidx.compose.runtime.Composable
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.time.TimeZone

@Composable
fun ScheduleBodyCard(schedule: Schedule, timeZone: TimeZone){
    if(timeZone == TimeZone.MORNING){
        ScheduleMorningCard(schedule = schedule)
    }
    else if(timeZone == TimeZone.EVENING){
        ScheduleEveningCard(schedule = schedule)
    }
}