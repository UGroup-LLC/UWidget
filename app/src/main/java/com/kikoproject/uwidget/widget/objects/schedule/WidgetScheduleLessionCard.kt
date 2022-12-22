package com.kikoproject.uwidget.widget.objects.schedule

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.layout.*
import com.kikoproject.uwidget.main.curSchedule
import com.kikoproject.uwidget.main.options
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.models.schedules.options.ScheduleOptions
import com.kikoproject.uwidget.utils.getCloseTimeRange
import com.kikoproject.uwidget.utils.getClosestLesion
import com.kikoproject.uwidget.utils.toTimeRange
import com.kikoproject.uwidget.widget.objects.WidgetText
import java.time.Duration
import java.time.LocalTime

@Composable
fun WidgetScheduleLesionCard(schedule: Schedule = curSchedule!!, context: Context) {
    if (options?.scheduleDayLesionSettings?.lesionEndTitleVisible != false) {
        val nowTime = LocalTime.now()

        val nextSchedule = schedule.getClosestLesion(1)

        schedule.Time.toTimeRange()?.let {
            val endLesionTime = nowTime.getCloseTimeRange(it)
            val duration = Duration.between(
                nowTime,
                endLesionTime?.endInclusive ?: LocalTime.MAX
            )
            val time = LocalTime.of(duration.toHours().toInt(), (duration.toMinutes() % 60).toInt())

            DrawText(
                titleText = "@@До конца пары: @$time@",
                nextSchedule = nextSchedule,
                context = context,
                schedule = schedule,
                options = options
            )
            return
        }

        DrawText(
            titleText = "@Произошла ошибка@",
            nextSchedule = nextSchedule,
            context = context,
            schedule = schedule,
            options = options
        )
    }
}

@Composable
fun DrawText(
    titleText: String,
    nextSchedule: String?,
    context: Context,
    schedule: Schedule,
    options: ScheduleOptions?
) {
        if (schedule.getClosestLesion() != null) {
            WidgetText(
                text = titleText,
                schedule = schedule,
                context = context,
                options = options
            )
        }
        WidgetText(
            text = "Следующее занятие: @${nextSchedule ?: "Нет"}@",
            schedule = schedule,
            context = context,
            options = options
        )
}