package com.kikoproject.uwidget.objects.schedules

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.objects.text.colorize
import com.kikoproject.uwidget.utils.getCloseTimeRange
import com.kikoproject.uwidget.utils.getClosestLesion
import com.kikoproject.uwidget.utils.toTimeRange
import java.time.Duration
import java.time.LocalTime
import java.util.*

@Composable
fun ScheduleLesionCard(schedule: Schedule) {
    if (schedule.Options!!.scheduleDayLesionSettings.lesionEndTitleVisible) {
        val day =
            (Calendar.getInstance() as GregorianCalendar).toZonedDateTime().dayOfWeek.ordinal

        val nowTime = LocalTime.now()

        val nextSchedule = schedule.getClosestLesion(1)

        schedule.Time.toTimeRange()?.let {
            nowTime.getCloseTimeRange(it)
            val duration = Duration.between(
                nowTime,
                nowTime.getCloseTimeRange(it)?.endInclusive ?: LocalTime.MAX
            )
            val time = LocalTime.of(duration.toHours().toInt(), (duration.toMinutes() % 60).toInt())

            DrawText("@@До конца пары: @$time@".colorize(), nextSchedule)
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
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.surface
    )
    Text(
        text = "Следующее занятие: @${nextSchedule ?: "Нет"}@".colorize(),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.surface
    )
}