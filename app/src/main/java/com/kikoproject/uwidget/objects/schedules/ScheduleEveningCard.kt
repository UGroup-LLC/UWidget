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
import com.kikoproject.uwidget.objects.text.colorize
import java.util.*

@Composable
fun ScheduleEveningCard(schedule: Schedule){
    if (schedule.Options!!.scheduleEveningSettings.allScheduleVisible){
        var scheduleText = ""
        var day = (Calendar.getInstance() as GregorianCalendar).toZonedDateTime().dayOfWeek.ordinal+1
        if(day == 7){
            day = 0
        }
        val daySchedule = schedule.Schedule[day.toString()]!!
        daySchedule.forEachIndexed { index, lession ->
            scheduleText += "@${schedule.Time[index].replace("..", " - ")})@ $lession" + if(index != daySchedule.lastIndex) "\n" else ""
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = "Занятия на завтра",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.surface
        )
        Text(
            text = scheduleText.colorize(),
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.surface
        )
    }
}