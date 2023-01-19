package com.kikoproject.uwidget.widget

import android.content.Context
import android.content.ContextParams
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.widget.RemoteViews
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.RECEIVER_NOT_EXPORTED
import androidx.core.content.ContextCompat.registerReceiver
import androidx.core.graphics.ColorUtils
import androidx.glance.*
import androidx.glance.appwidget.*
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.layout.*
import androidx.room.Room
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.localdb.MainDataBase
import com.kikoproject.uwidget.main.*
import com.kikoproject.uwidget.time.getTimeZone
import com.kikoproject.uwidget.ui.theme.MainThemes
import com.kikoproject.uwidget.ui.theme.themeAppMode
import com.kikoproject.uwidget.ui.theme.themeBackgroundColor
import com.kikoproject.uwidget.utils.toStandardColor
import com.kikoproject.uwidget.widget.objects.WidgetTitleSchedule
import com.kikoproject.uwidget.widget.objects.schedule.WidgetScheduleBodyCard
import java.util.*

var textThemeWidget = Color(0xFFEDF2F8)

class MainWidget : GlanceAppWidget() {
    override val sizeMode: SizeMode = SizeMode.Exact

    @Composable
    override fun Content() {
        val size = LocalSize.current
        val context = LocalContext.current
        roomDb = Room.databaseBuilder(
            context.applicationContext,
            MainDataBase::class.java, "main_database"
        ).allowMainThreadQueries().build()
        options = roomDb!!.optionsDao().get().SchedulesOptions
        curUser = roomDb!!.userDao().get()

        textThemeWidget =
            if (ColorUtils.calculateLuminance(options!!.generalSettings.backgroundColor.toArgb()) < 0.5) {
                Color(0xFFEDF2F8)
            } else {
                Color(0xFF000000)
            }

        val service = Intent(context, WidgetUpdateService::class.java)

        try {
            context.startService(service)
        } catch (exception: Exception) {
            Log.v("Widget", "Caught service error")
        }
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
        BackgroundView(context) {
            Column(modifier = GlanceModifier.padding(horizontal = 30.dp, vertical = 30.dp)) {
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
                Spacer(modifier = GlanceModifier.padding(4.dp))
                if (timeZone != null) {
                    if (size.height > 250.dp) {
                        WidgetScheduleBodyCard(
                            schedule = curSchedule!!,
                            timeZone = timeZone,
                            context = context
                        )
                    }
                    else{

                    }
                }
            }
        }
    }


    @GlanceComposable
    @Composable
    private fun BackgroundView(context: Context, content: @Composable () -> Unit) {

        val backgroundView =
            RemoteViews(context.packageName, R.layout.widget_background_layout)

        backgroundView.setInt(
            R.id.backOfMainW,
            "setColorFilter",
            options?.generalSettings?.backgroundColor?.toArgb()
                ?: ContextCompat.getColor(context, R.color.statusBar)
        )

        backgroundView.setInt(
            R.id.strokeMainW,
            "setColorFilter",
            if (options?.generalSettings?.isBorderVisible == true) {
                options?.generalSettings?.borderColor?.toArgb()
                    ?: ContextCompat.getColor(context, R.color.iconBack)
            } else {
                options?.generalSettings?.backgroundColor?.toArgb()
                    ?: ContextCompat.getColor(context, R.color.statusBar)
            }
        )
        backgroundView.setViewPadding(
            R.id.backOfMainW,
            options!!.generalSettings.borderThickness.toInt(),
            options!!.generalSettings.borderThickness.toInt(),
            options!!.generalSettings.borderThickness.toInt(),
            options!!.generalSettings.borderThickness.toInt()
        )
        AndroidRemoteViews(
            remoteViews = backgroundView,
            containerViewId = R.id.mainBack
        ) {
            content()
        }
    }
}

class MainWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = MainWidget()
}