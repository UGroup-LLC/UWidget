package com.kikoproject.uwidget.widget.objects.schedule

import android.content.Context
import androidx.compose.runtime.Composable
import com.kikoproject.uwidget.main.curSchedule
import com.kikoproject.uwidget.main.options
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.utils.getCloseTimeBetweenRange
import com.kikoproject.uwidget.utils.getClosestLesion
import com.kikoproject.uwidget.utils.toTimeRange
import java.time.Duration
import java.time.LocalTime

@Composable
fun WidgetScheduleRestCard(schedule: Schedule = curSchedule!!, context: Context) {
    if (options?.scheduleDayLesionSettings?.lesionEndTitleVisible != false) {
        val nowTime = LocalTime.now()

        val nextSchedule = schedule.getClosestLesion()

        schedule.Time.toTimeRange()?.let {
            val startLesionTime = nowTime.getCloseTimeBetweenRange(it)
            val duration = Duration.between(
                nowTime,
                startLesionTime?.endInclusive ?: LocalTime.MAX
            )
            val time = LocalTime.of(duration.toHours().toInt(), (duration.toMinutes() % 60).toInt())

            DrawText(
                "@@До начала: @$time@", nextSchedule,
                context = context,
                schedule = schedule,
                options = options
            )
            return
        }

        DrawText(
            "@Произошла ошибка@", nextSchedule,
            context = context,
            schedule = schedule,
            options = options
        )
    }
}
