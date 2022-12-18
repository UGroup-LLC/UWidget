package com.kikoproject.uwidget.widget

import android.content.Context
import android.widget.RemoteViews
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.appwidget.AndroidRemoteViews
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.room.Room
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.localdb.MainDataBase
import com.kikoproject.uwidget.main.curSchedule
import com.kikoproject.uwidget.main.options
import com.kikoproject.uwidget.main.prefs
import com.kikoproject.uwidget.main.roomDb
import com.kikoproject.uwidget.models.schedules.options.ScheduleOptions
import com.kikoproject.uwidget.time.getTimeZone
import com.kikoproject.uwidget.utils.toStandardColor
import com.kikoproject.uwidget.widget.objects.WidgetTitleSchedule
import com.kikoproject.uwidget.widget.objects.schedule.WidgetScheduleBodyCard

lateinit var textStyleCaption: TextStyle

class MainWidget : GlanceAppWidget() {

    @Composable
    override fun Content() {
        val context = LocalContext.current
        Firebase.analytics

        prefs = context.getSharedPreferences(
            context.packageName, Context.MODE_PRIVATE
        )

        curSchedule = prefs?.getString(
            "mainSchedule",
            "0"
        )?.let {
            roomDb?.scheduleDao()?.getWithId(
                it
            )
        }

        options = roomDb?.optionsDao()?.get()?.SchedulesOptions

        val timeZone = curSchedule?.getTimeZone()

        BackgroundView(context)

        LazyColumn(
            modifier = GlanceModifier.fillMaxSize()
                .padding(horizontal = 45.dp, vertical = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                if (curSchedule != null && timeZone != null) {
                    WidgetTitleSchedule(
                        schedule = curSchedule!!,
                        timeZone = timeZone,
                        context = context
                    )
                } else {
                    WidgetTitleSchedule(
                        text = "Создайте расписание",
                        context = context
                    )
                }
            }
            item { Spacer(modifier = GlanceModifier.padding(4.dp)) }
            item {
                if (timeZone != null) {
                    WidgetScheduleBodyCard(
                        schedule = curSchedule!!,
                        timeZone = timeZone,
                        context = context
                    )
                }
            }
        }
    }
}

@Composable
private fun BackgroundView(context: Context) {
    val backgroundView =
        RemoteViews(context.packageName, R.layout.widget_background_layout)
    backgroundView.setInt(
        R.id.backOfMainW,
        "setColorFilter",
        options?.generalSettings?.backgroundColor?.toArgb() ?: context.resources.getColor(R.color.statusBar)
    )
    backgroundView.setInt(
        R.id.strokeMainW,
        "setColorFilter",
        options?.generalSettings?.borderColor?.toArgb() ?: context.resources.getColor(R.color.iconBack)
    )
    backgroundView.setViewPadding(R.id.backOfMainW, 5, 5, 5, 5)

    AndroidRemoteViews(remoteViews = backgroundView)
}

class MainWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = MainWidget()
}

