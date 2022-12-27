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
import com.kikoproject.uwidget.utils.distinctLesions
import com.kikoproject.uwidget.utils.distinctTime
import java.util.*

@Composable
fun ScheduleEveningCard(schedule: Schedule = curSchedule!!) {
    if (options?.scheduleEveningSettings?.allScheduleVisible != false) {
        var scheduleText = ""
        var title = "Занятия на завтра".colorize()
        var day =            (Calendar.getInstance() as GregorianCalendar).toZonedDateTime().dayOfWeek.ordinal + 1
        if (day == 7) {
            day = 0
        }
        val daySchedule = schedule.Schedule[day.toString()]!!.distinctLesions()
        val distinctTime = schedule.Time.distinctTime(schedule.Schedule[day.toString()]!!)

        daySchedule.forEachIndexed { index, lesion ->

            scheduleText += "@${
                if (distinctTime.getOrNull(index) != null) {
                    distinctTime[index].toString().replace(
                        "..",
                        " - "
                    )
                } else { 
                    "[TE]" // Если произошла какая то ошибка при отправке времени
                }

            }@\n$lesion" + if (index != daySchedule.lastIndex) "\n\n" else ""
        }
        val isArrayNotEmpty = daySchedule.deleteWhitespaces().isNotEmpty()
        if (!isArrayNotEmpty) {
            title = "На завтра занятий нет\n@Хорошего вечера!@".colorize()
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth()
        )
        if (isArrayNotEmpty) {
            Text(
                text = scheduleText.colorize(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}