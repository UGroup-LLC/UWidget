package com.kikoproject.uwidget.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.networking.OnlineContent
import com.kikoproject.uwidget.networking.getNextUserSchedule
import com.kikoproject.uwidget.objects.MainHeader
import com.kikoproject.uwidget.objects.home_widget.HomeWidgetPreview
import com.kikoproject.uwidget.objects.schedules.ScheduleBodyCard
import com.kikoproject.uwidget.objects.schedules.TitleSchedule
import com.kikoproject.uwidget.objects.text.colorize
import com.kikoproject.uwidget.time.getTimeZone
import com.kikoproject.uwidget.widget.MainWidget
import com.kikoproject.uwidget.widget.MainWidgetReceiver
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

/**
 * Главное окно приложения где отображается превью виджета, кнопки настроек и тд
 * @author Kiko
 */
@Composable
fun DashboardActivity() {
    OnlineContent(user = curUser, content = { myScheduleUser, myScheduleAdmin ->
        val context = LocalContext.current
        val account = GoogleSignIn.getLastSignedInAccount(context)

        runBlocking {
            MainWidget().updateAll(context)
        }

        curSchedule = prefs?.getString(
            "mainSchedule",
            getNextUserSchedule(
                mySchedulesAdmin = myScheduleAdmin,
                mySchedulesUser = myScheduleUser
            )?.ID
        )?.let {
            roomDb!!.scheduleDao().getWithId(
                it
            )
        }

        val timeZone = curSchedule?.getTimeZone()

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp, vertical = 10.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                MainHeader(account = account)
                Text(
                    text = "Текущее расписание: @${curSchedule?.Name ?: "Нету"}@".colorize()
                )
                if (timeZone != null) {
                    HomeWidgetPreview(timeZone = timeZone)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(50.dp),
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.padding(vertical = 5.dp, horizontal = 15.dp)
                ) {
                    DashboardButtons()
                }


                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    var isWidgetCreated: MutableState<Boolean>
                    runBlocking {
                        isWidgetCreated = mutableStateOf(
                            GlanceAppWidgetManager(context).getGlanceIds(
                                MainWidget::class.java
                            ).isNotEmpty())
                    }

                    if (!isWidgetCreated.value) {

                        Text(text = "Добавьте виджет на главный экран")
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                GlanceAppWidgetManager(context).requestPinGlanceAppWidget(
                                    MainWidgetReceiver::class.java,
                                    MainWidget()
                                )
                                isWidgetCreated.value = true
                            }) {
                            Text(text = "Добавить")
                        }
                    }
                }
            }
        }
    })
}

@Composable
private fun DashboardButtons() {
    FloatingActionButton(
        onClick = { navController.navigate(ScreenNav.WidgetOptionsNav.route) },
        modifier = Modifier.requiredSize(65.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(19.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_pencil),
            modifier = Modifier.padding(21.dp),
            contentDescription = null,
            tint = Color.Black.copy(0.4f),
        )
    }
    FloatingActionButton(
        onClick = { navController.navigate(ScreenNav.AllSchedulesNav.route) },
        modifier = Modifier.requiredSize(65.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(19.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_schedules),
            modifier = Modifier.padding(21.dp),
            contentDescription = null,
            tint = Color.Black.copy(0.4f),
        )
    }
    FloatingActionButton(
        onClick = { navController.navigate(ScreenNav.OptionsNav.route) },
        modifier = Modifier.requiredSize(65.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(19.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_settings),
            modifier = Modifier.padding(20.dp),
            contentDescription = null,
            tint = Color.Black.copy(0.4f),
        )
    }
}
