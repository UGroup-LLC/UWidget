package com.kikoproject.uwidget.widget.objects.schedule

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.padding
import com.kikoproject.uwidget.main.curSchedule
import com.kikoproject.uwidget.main.options
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.models.schedules.options.ScheduleOptions
import com.kikoproject.uwidget.utils.deleteWhitespaces
import com.kikoproject.uwidget.utils.toLocalTime
import com.kikoproject.uwidget.utils.toTimeRange
import com.kikoproject.uwidget.widget.objects.WidgetText
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun WidgetScheduleMorningCard(schedule: Schedule = curSchedule!!, context: Context) {
    val day = (Calendar.getInstance() as GregorianCalendar).toZonedDateTime().dayOfWeek.ordinal
    var lesion: String
    var scheduleText = ""
    Column {

        // Отображение времени до первой пары
        if (options?.scheduleMorningSettings?.beforeLesionVisible != false &&
            schedule.Schedule[day.toString()]?.deleteWhitespaces()?.isNotEmpty() == true
        ) {

            val nowTime = LocalTime.now()
            val lesionTime = schedule.Time.first().toTimeRange().start.toLocalTime()

            val duration = Duration.between(nowTime, lesionTime)

            val time = LocalTime.of(duration.toHours().toInt(), (duration.toMinutes() % 60).toInt())
            val formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"))

            WidgetText(
                text = "Время до первой пары",
                context = context,
                options = options
            )
            WidgetText(
                text = "@${formattedTime}@",
                context = context,
                options = options
            )
        }

        // Отображение первой пары
        if (options?.scheduleMorningSettings?.nextPairVisible != false) {
            var title = "@@Первое занятие"
            try {
                lesion = schedule.Schedule[day.toString()]!!
                    .deleteWhitespaces()
                    .first()
                    .replace("..", " - ")

                val time = schedule.Time.first().replace("..", " - ")
                scheduleText = "@$time@ $lesion"
            } catch (exception: Exception) {
                title = "Сегодня занятий нет\n@Хорошего дня!@"
            }
            Spacer(modifier = GlanceModifier.padding(4.dp))
            WidgetText(
                text = title,
                context = context,
                options = options
            )
            if (scheduleText != "") {
                WidgetText(
                    text = scheduleText,
                    context = context,
                    options = options
                )
            }
        }
    }
}