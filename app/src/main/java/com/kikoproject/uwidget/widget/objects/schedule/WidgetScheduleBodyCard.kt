package com.kikoproject.uwidget.widget.objects.schedule

import android.content.Context
import androidx.compose.runtime.Composable
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.time.TimeZone

@Composable
fun WidgetScheduleBodyCard(schedule: Schedule, timeZone: TimeZone, context: Context){
    when(timeZone)
    {
        TimeZone.MORNING -> {
            WidgetScheduleMorningCard(context = context)
        }
        TimeZone.EVENING -> {
            WidgetScheduleEveningCard(context = context)
        }
        TimeZone.DAY_LESION -> {
            WidgetScheduleLesionCard(context = context)
        }
        TimeZone.DAY_REST -> {
            WidgetScheduleRestCard(context = context)
        }
        else -> {

        }
    }
}