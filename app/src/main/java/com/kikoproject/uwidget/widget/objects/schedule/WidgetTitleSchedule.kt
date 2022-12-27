package com.kikoproject.uwidget.widget.objects

import android.content.Context
import android.text.Html
import android.widget.RemoteViews
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.glance.appwidget.AndroidRemoteViews
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.main.curSchedule
import com.kikoproject.uwidget.main.options
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.models.schedules.options.ScheduleOptions
import com.kikoproject.uwidget.objects.text.colorizeWidgetText
import com.kikoproject.uwidget.objects.text.variablize
import com.kikoproject.uwidget.time.TimeZone
import com.kikoproject.uwidget.utils.getClosestLesion
import com.kikoproject.uwidget.utils.toStandardColor

/**
 * Выбирает что за текст необходимо отображать в превью в заголовке виджета
 *
 * @author Kiko
 */
@Composable
fun WidgetTitleSchedule(schedule: Schedule, timeZone: TimeZone, context: Context) {
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
            null
        }
    }

    if ((timeZone == TimeZone.MORNING && options?.scheduleMorningSettings?.morningVisible != false)
        || timeZone == TimeZone.DAY_LESION || timeZone == TimeZone.DAY_REST || timeZone == TimeZone.ERROR ||
        (timeZone == TimeZone.EVENING && options?.scheduleEveningSettings?.eveningTitleVisible != false)
    ) {
        WidgetTitleText(text = text, context = context, options = options)
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
fun WidgetTitleSchedule(text: String, context: Context) {
    val titleView = RemoteViews(context.packageName, R.layout.widget_title_layout)
    titleView.setInt(
        R.id.titleView,
        "setColorFilter",
        options?.generalSettings?.borderColor?.toArgb() ?: ContextCompat.getColor(context,R.color.iconBack)
    )
    titleView.setTextViewText(R.id.titleText, text)
    AndroidRemoteViews(remoteViews = titleView)
}