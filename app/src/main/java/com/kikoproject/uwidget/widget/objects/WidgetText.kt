package com.kikoproject.uwidget.widget.objects

import android.content.Context
import android.text.Html
import android.text.Spanned
import android.widget.RemoteViews
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.AndroidRemoteViews
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxWidth
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.main.curSchedule
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.models.schedules.options.ScheduleOptions
import com.kikoproject.uwidget.objects.text.colorizeWidgetText
import com.kikoproject.uwidget.objects.text.variablize
import com.kikoproject.uwidget.utils.toStandardColor

@Composable
fun WidgetTitleText(
    text: String?,
    schedule: Schedule,
    context: Context,
    options: ScheduleOptions?
) {
    val textView = RemoteViews(context.packageName, R.layout.widget_title_layout)
    textView.setInt(
        R.id.titleView,
        "setColorFilter",
        options?.generalSettings?.borderColor?.toStandardColor() ?: context.resources.getColor(R.color.iconBack)
    )

    textView.setTextColor(
        R.id.titleText,
        options?.generalSettings?.textColor?.toArgb()
            ?: Color.White.toStandardColor()
    )
    val colorText = text?.colorizeWidgetText(schedule = schedule)
    textView.setTextViewText(
        R.id.titleText, Html.fromHtml(
            colorText ?: "Хорошего отдыха, @%n!@".variablize().colorizeWidgetText(
                schedule = schedule
            )
        )
    )
    Box(modifier = GlanceModifier.fillMaxWidth()) {
        AndroidRemoteViews(remoteViews = textView)
    }
}

@Composable
fun WidgetText(
    text: String?,
    schedule: Schedule,
    context: Context,
    options: ScheduleOptions?,
    color: Color? = options?.generalSettings?.textColor
) {
    val textView = RemoteViews(context.packageName, R.layout.widget_text_layout)
    textView.setTextColor(
        R.id.mainText,
        color?.toArgb()
            ?: Color.White.toArgb()
    )
    val colorText =
        text?.variablize(null)?.colorizeWidgetText(schedule = schedule)
    textView.setTextViewText(R.id.mainText, Html.fromHtml(colorText))
    AndroidRemoteViews(remoteViews = textView)
}