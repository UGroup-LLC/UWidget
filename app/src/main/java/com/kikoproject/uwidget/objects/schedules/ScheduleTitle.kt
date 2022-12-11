package com.kikoproject.uwidget.objects.schedules

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kikoproject.uwidget.models.User
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.objects.colorize
import com.kikoproject.uwidget.objects.text.variablize
import com.kikoproject.uwidget.time.TimeZone
import com.kikoproject.uwidget.time.getTimeZone


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
fun TitleShedule(user: User, schedule: Schedule) {
    var text = ""
    val timeZone = getTimeZone(schedule)
    if (timeZone == TimeZone.MORNING) {
        text = schedule.Options?.scheduleMorningSettings?.morningTitle.toString()
    } else if (timeZone == TimeZone.EVENING) {
        text = schedule.Options?.scheduleEveningSettings?.eveningTitleText.toString().variablize()
    } else {
        text = "@Извините,@ произошла ошибка"
    }

    Text(
        text = text.colorize(),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.surface
    )
}

/**
 * Переопределенный метод используемый для отображения заголовка в превью с кастом текстом
 *
 * @param text текст заголовка
 *
 * @author Kiko
 */
@Composable
fun TitleShedule(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.surface
    )
}