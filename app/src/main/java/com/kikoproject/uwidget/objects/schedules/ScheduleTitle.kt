package com.kikoproject.uwidget.objects.schedules

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.kikoproject.uwidget.main.options
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.objects.text.colorize
import com.kikoproject.uwidget.objects.text.variablize
import com.kikoproject.uwidget.time.TimeZone
import com.kikoproject.uwidget.utils.getClosestLesion
import java.util.*


/**
 * Выбирает что за текст необходимо отображать в превью в заголовке
 *
 * @param schedule расписание
 * @param timeZone временная зона дня
 *
 * @author Kiko
 */
@Composable
fun TitleSchedule(schedule: Schedule, timeZone: TimeZone) {
    val text = when (timeZone) {
        TimeZone.MORNING -> {
            options?.scheduleMorningSettings?.morningTitle.toString().variablize()
        }
        TimeZone.EVENING -> {
            options?.scheduleEveningSettings?.eveningTitleText.toString().variablize()
        }
        TimeZone.DAY_LESION -> {
            schedule.getClosestLesion()
        }
        TimeZone.DAY_REST -> {
            "Хорошего отдыха, @%n!@".variablize()
        }
        TimeZone.ERROR -> {
            "@Извините,@ произошла ошибка"
        }
    }

    if ((timeZone == TimeZone.MORNING && options?.scheduleMorningSettings?.morningVisible != false)
        || timeZone == TimeZone.DAY_LESION || timeZone == TimeZone.DAY_REST || timeZone == TimeZone.ERROR ||
        (timeZone == TimeZone.EVENING && options?.scheduleEveningSettings?.eveningTitleVisible != false)
    ) {
        Text(
            text = text?.colorize() ?: "Хорошего отдыха, @%n!@".variablize().colorize(),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
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
fun TitleSchedule(text: String, color: Color = MaterialTheme.colorScheme.onSurface) {
    Text(
        text = text,
        color = color,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}