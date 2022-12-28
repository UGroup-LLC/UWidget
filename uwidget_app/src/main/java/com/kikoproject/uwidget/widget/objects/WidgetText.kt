package com.kikoproject.uwidget.widget.objects

import android.content.Context
import android.text.Html
import android.text.Spanned
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.widget.RemoteViews
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.text.HtmlCompat
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
import com.kikoproject.uwidget.widget.textThemeWidget

@Composable
fun WidgetTitleText(
    text: String?,
    context: Context,
    options: ScheduleOptions?
) {
    val textView = RemoteViews(context.packageName, R.layout.widget_title_layout)
    textView.setInt(
        R.id.titleView,
        "setColorFilter",
        options!!.generalSettings.borderColor.toStandardColor()
    )

    textView.setTextColor(
        R.id.titleText,
        textThemeWidget.toArgb()
    )
    val colorText = text?.colorizeWidgetText()
    textView.setTextViewText(
        R.id.titleText, HtmlCompat.fromHtml(
            colorText ?: "Хорошего отдыха, @%n!@".variablize().colorizeWidgetText(),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    )

    textView.setTextViewTextSize(
        R.id.titleText,
        COMPLEX_UNIT_SP,
        options.generalSettings.textSize.toFloat()
    )

    Box(modifier = GlanceModifier.fillMaxWidth()) {
        AndroidRemoteViews(remoteViews = textView)
    }
}

@Composable
fun WidgetText(
    text: String?,
    context: Context,
    options: ScheduleOptions?,
    color: Color? = textThemeWidget
) {
    val remoteTextView = RemoteViews(context.packageName, R.layout.widget_text_layout)
    remoteTextView.setTextColor(
        R.id.mainText,
        color?.toArgb()
            ?: Color.White.toArgb()
    )
    val colorText =
        text?.variablize(null)?.colorizeWidgetText()
    remoteTextView.setTextViewTextSize(
        R.id.mainText,
        COMPLEX_UNIT_SP,
        options!!.generalSettings.textSize.toFloat()
    )
    remoteTextView.setTextViewText(
        R.id.mainText,
        HtmlCompat.fromHtml(colorText ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)
    )
    AndroidRemoteViews(remoteViews = remoteTextView)
}