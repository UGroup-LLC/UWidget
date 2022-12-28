package com.kikoproject.uwidget.objects.schedules

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kikoproject.uwidget.main.curSchedule
import com.kikoproject.uwidget.main.options
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.objects.text.colorize
import com.kikoproject.uwidget.utils.getCloseTimeBetweenRange
import com.kikoproject.uwidget.utils.getClosestLesion
import com.kikoproject.uwidget.utils.toTimeRange
import java.time.Duration
import java.time.LocalTime
import java.util.*

@Composable
fun ScheduleRestCard(schedule: Schedule = curSchedule!!) {
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

            DrawText("@@До начала: @$time@".colorize(), nextSchedule)
            return
        }

        DrawText("@Произошла ошибка@".colorize(), nextSchedule)
    }
}

@Composable
private fun DrawText(titleText: AnnotatedString, nextSchedule: String?) {
    Spacer(modifier = Modifier.padding(4.dp))
    Text(
        text = titleText,
        modifier = Modifier.fillMaxWidth()
    )
    Text(
        text = "Следующее занятие: @${nextSchedule ?: "Нет"}@".colorize(),
        modifier = Modifier.fillMaxWidth()
    )
}