package com.kikoproject.uwidget.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.glance.appwidget.updateAll
import com.github.alexzhirkevich.customqrgenerator.QrCodeGenerator
import com.github.alexzhirkevich.customqrgenerator.QrData
import com.github.alexzhirkevich.customqrgenerator.QrOptions
import com.github.alexzhirkevich.customqrgenerator.style.*
import com.github.alexzhirkevich.customqrgenerator.vector.QrCodeDrawable
import com.github.alexzhirkevich.customqrgenerator.vector.QrVectorOptions
import com.github.alexzhirkevich.customqrgenerator.vector.style.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.networking.OnlineContent
import com.kikoproject.uwidget.networking.getNextUserSchedule
import com.kikoproject.uwidget.objects.MainHeader
import com.kikoproject.uwidget.objects.camera.JoinQRCodeScanner
import com.kikoproject.uwidget.objects.schedules.ScheduleBodyCard
import com.kikoproject.uwidget.objects.schedules.TitleSchedule
import com.kikoproject.uwidget.objects.text.colorize
import com.kikoproject.uwidget.time.getTimeZone
import com.kikoproject.uwidget.ui.theme.Main
import com.kikoproject.uwidget.widget.MainWidget
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

        if (curSchedule != null) {
            if (prefs?.contains("mainSchedule") == false) {
                prefs?.edit()?.putString("mainSchedule", curSchedule!!.ID)?.apply()
            }
        }

        val timeZone = curSchedule?.getTimeZone()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(horizontal = 30.dp, vertical = 10.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                MainHeader(account = account)
                Text(
                    text = "Текущее расписание: @${curSchedule?.Name ?: "Нету"}@".colorize(),
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.surface
                )
                Card( // content
                    shape = RoundedCornerShape(20.dp),
                    contentColor = MaterialTheme.colors.background,
                    backgroundColor = MaterialTheme.colors.background,
                    border = BorderStroke(1.dp, MaterialTheme.colors.primary)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 45.dp, vertical = 20.dp)
                    ) {
                        Card(
                            shape = RoundedCornerShape(45.dp),
                            backgroundColor = MaterialTheme.colors.primary.copy(0.25f)
                        ) {
                            if (curSchedule != null && timeZone != null) {
                                TitleSchedule(
                                    schedule = curSchedule!!,
                                    timeZone = timeZone
                                )
                            } else {
                                TitleSchedule("Создайте расписание")
                            }
                        }
                        if (timeZone != null) {
                            ScheduleBodyCard(schedule = curSchedule!!, timeZone)
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(50.dp),
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.padding(vertical = 5.dp, horizontal = 15.dp)
                ) {
                    FloatingActionButton(
                        onClick = { navController.navigate(ScreenNav.WidgetOptionsNav.route) },
                        modifier = Modifier.requiredSize(65.dp),
                        backgroundColor = MaterialTheme.colors.primary,
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
                        backgroundColor = MaterialTheme.colors.primary,
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
                        backgroundColor = MaterialTheme.colors.primary,
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
            }
        }
    })
}
