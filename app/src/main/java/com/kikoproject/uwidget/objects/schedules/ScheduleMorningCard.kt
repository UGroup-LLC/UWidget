package com.kikoproject.uwidget.objects.schedules

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.objects.colorize
import java.util.*

@Composable
fun ScheduleMorningCard(schedule: Schedule){
    // Отображение времени до первой пары
    if (schedule.Options!!.scheduleMorningSettings.beforeLesionVisible){
        var scheduleText = ""
        val day = (Calendar.getInstance() as GregorianCalendar).toZonedDateTime().dayOfWeek.ordinal
        val time = schedule.Time.first().replace("..", " - ")
        val lession = schedule.Schedule.get(day.toString())!!.first().replace("..", " - ")
        scheduleText += "@$time)@ $lession"
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = "Время до первой пары",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.surface
        )
        Text(
            text = scheduleText.colorize(),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.surface
        )
    }

    // Отображение первой пары
    if (schedule.Options!!.scheduleMorningSettings.nextPairVisible){
        var scheduleText = ""
        val day = (Calendar.getInstance() as GregorianCalendar).toZonedDateTime().dayOfWeek.ordinal
        val time = schedule.Time.first().replace("..", " - ")
        val lession = schedule.Schedule.get(day.toString())!!.first().replace("..", " - ")
        scheduleText += "@$time)@ $lession"
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = "Первое занятие",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.surface
        )
        Text(
            text = scheduleText.colorize(),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.surface
        )
    }
}