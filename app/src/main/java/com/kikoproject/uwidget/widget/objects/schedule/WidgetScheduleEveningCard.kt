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
import com.kikoproject.uwidget.utils.distinctLesions
import com.kikoproject.uwidget.utils.distinctTime
import com.kikoproject.uwidget.widget.objects.WidgetText
import java.util.*

@Composable
fun WidgetScheduleEveningCard(schedule: Schedule = curSchedule!!, context: Context) {
    if (options?.scheduleEveningSettings?.allScheduleVisible != false) {
        var scheduleText = ""
        var title = "Занятия на завтра"
        var day = (Calendar.getInstance() as GregorianCalendar).toZonedDateTime().dayOfWeek.ordinal + 1
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
            title = "На завтра занятий нет\n@Хорошего вечера!@"
        }
        Column() {
            WidgetText(
                text = title,
                schedule = schedule,
                context = context,
                options = options
            )
            if (isArrayNotEmpty) {
                WidgetText(
                    text = scheduleText,
                    schedule = schedule,
                    context = context,
                    options = options
                )
            }
        }
    }
}