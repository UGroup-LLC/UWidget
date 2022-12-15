package com.kikoproject.uwidget.objects.schedules

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kikoproject.uwidget.models.User
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.objects.text.colorize
import com.kikoproject.uwidget.objects.text.variablize
import com.kikoproject.uwidget.time.TimeZone
import com.kikoproject.uwidget.utils.getCloseTimeRange
import com.kikoproject.uwidget.utils.getClosestLesion
import com.kikoproject.uwidget.utils.toTimeRange
import java.time.LocalTime
import java.util.*


/**
 * Выбирает что за текст необходимо отображать в превью в заголовке
 *
 * @param user пользователь имя которого может отобразиться в заголовке
 *
 * @author Kiko
 *
 * @exception TODO("НЕОБХОДИМО ПЕРЕНЕСТИ ЭТО В ОТДЕЛЬНЫЙ МЕТОД")
 */
@Composable
fun TitleSchedule(schedule: Schedule, timeZone: TimeZone) {
    val text = when (timeZone) {
        TimeZone.MORNING -> {
            schedule.Options?.scheduleMorningSettings?.morningTitle.toString().variablize()
        }
        TimeZone.EVENING -> {
            schedule.Options?.scheduleEveningSettings?.eveningTitleText.toString().variablize()
        }
        TimeZone.DAY_LESION -> {
            schedule.getClosestLesion()
        }
        TimeZone.ERROR -> {
            null
        }
        else -> {
            null
        }
    }

    if ((timeZone == TimeZone.MORNING && schedule.Options!!.scheduleMorningSettings.morningVisible) ||
        timeZone == TimeZone.DAY_LESION ||
        (timeZone == TimeZone.EVENING && schedule.Options!!.scheduleEveningSettings.eveningTitleVisible)
    ) {
        Text(
            text = text?.colorize() ?: "@Извините,@ произошла ошибка".colorize(),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.surface
        )
    }
}

/**
 * Переопределенный метод используемый для отображения заголовка в превью с кастом текстом
 *
 * @param text текст заголовка
 *
 * @author Kiko
 */
@Composable
fun TitleSchedule(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.surface
    )
}