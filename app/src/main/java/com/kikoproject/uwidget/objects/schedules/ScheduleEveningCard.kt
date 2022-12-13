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
import com.kikoproject.uwidget.utils.deleteWhitespaces
import java.util.*

@Composable
fun ScheduleEveningCard(schedule: Schedule){
    if (schedule.Options!!.scheduleEveningSettings.allScheduleVisible){
        var scheduleText = ""
        var title = "@@Занятия на завтра".colorize()
        var day = (Calendar.getInstance() as GregorianCalendar).toZonedDateTime().dayOfWeek.ordinal+1
        if(day == 7){
            day = 0
        }
        val daySchedule = schedule.Schedule[day.toString()]!!
        daySchedule.forEachIndexed { index, lession ->
            scheduleText += "@${schedule.Time[index].replace("..", " - ")}@ $lession" + if(index != daySchedule.lastIndex) "\n" else ""
        }
        val isArrayNotEmpty = daySchedule.deleteWhitespaces().isNotEmpty()
        if(!isArrayNotEmpty){
            title = "На завтра занятий нет\n@Хорошего вечера!@".colorize()
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.surface
        )
        if(isArrayNotEmpty) {
            Text(
                text = scheduleText.colorize(),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.surface
            )
        }
    }
}