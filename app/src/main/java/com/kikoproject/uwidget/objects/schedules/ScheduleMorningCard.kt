package com.kikoproject.uwidget.objects.schedules

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kikoproject.uwidget.main.curSchedule
import com.kikoproject.uwidget.main.options
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.models.schedules.options.ScheduleOptions
import com.kikoproject.uwidget.objects.text.colorize
import com.kikoproject.uwidget.utils.deleteWhitespaces
import com.kikoproject.uwidget.utils.toLocalTime
import com.kikoproject.uwidget.utils.toTimeRange
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun ScheduleMorningCard(schedule: Schedule? = curSchedule){
    val day = (Calendar.getInstance() as GregorianCalendar).toZonedDateTime().dayOfWeek.ordinal
    val lesion: String
    var scheduleText = ""

    // Отображение времени до первой пары
    if (options?.scheduleMorningSettings?.beforeLesionVisible != false &&
        schedule!!.Schedule[day.toString()]?.deleteWhitespaces()?.isNotEmpty() == true){

        val nowTime = LocalTime.now()
        val lesionTime = schedule.Time.first().toTimeRange().start.toLocalTime()

        val duration = Duration.between(nowTime, lesionTime)

        val time = LocalTime.of(duration.toHours().toInt(), (duration.toMinutes() % 60).toInt())
        val formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"))

        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = "Время до первой пары",
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "@${formattedTime}@".colorize(),
            modifier = Modifier.fillMaxWidth()
        )
    }

    // Отображение первой пары
    if (options?.scheduleMorningSettings?.nextPairVisible != false){
        var title = "@@Первое занятие".colorize()
        try {
            lesion = schedule!!.Schedule[day.toString()]!!
                .deleteWhitespaces()
                .first()
                .replace("..", " - ")

            val time = schedule.Time.first().replace("..", " - ")
            scheduleText = "@$time@ $lesion"
        }
        catch (exception: Exception){
            title = "Сегодня занятий нет\n@Хорошего дня!@".colorize()
        }

        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth()
        )
        if(scheduleText != "") {
            Text(
                text = scheduleText.colorize(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}