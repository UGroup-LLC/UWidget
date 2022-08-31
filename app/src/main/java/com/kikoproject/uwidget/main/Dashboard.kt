package com.kikoproject.uwidget.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.models.User
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.networking.OnlineContent
import com.kikoproject.uwidget.networking.getNextUserSchedule
import com.kikoproject.uwidget.objects.MainHeader
import com.kikoproject.uwidget.objects.coloredTitleText
import com.kikoproject.uwidget.time.TimeZone
import com.kikoproject.uwidget.time.getTimeZone

/**
 * Главное окно приложения где отображается превью виджета, кнопки настроек и тд
 * @author Kiko
 */
@Composable
fun DashboardActivity() {
    OnlineContent(user = curUser, content = { myScheduleUser, myScheduleAdmin ->
        val context = LocalContext.current
        val account = GoogleSignIn.getLastSignedInAccount(context)


        curSchedule = prefs.getString(
            "mainSchedule",
            getNextUserSchedule(
                mySchedulesAdmin = myScheduleAdmin,
                mySchedulesUser = myScheduleUser
            )?.ID
        )?.let {
            roomDb.scheduleDao().getWithId(
                it
            )
        }

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
                Card( // content
                    shape = RoundedCornerShape(20.dp),
                    contentColor = MaterialTheme.colors.background,
                    backgroundColor = MaterialTheme.colors.background,
                    border = BorderStroke(1.dp, MaterialTheme.colors.primary)
                ) {
                    Card(
                        shape = RoundedCornerShape(45.dp),
                        backgroundColor = MaterialTheme.colors.primary.copy(0.25f),
                        modifier = Modifier.padding(horizontal = 45.dp, vertical = 20.dp)
                    ) {
                        if(curSchedule != null) {
                            TitleShedule(user = curUser, schedule = curSchedule!!)
                        }
                        else
                        {
                            TitleShedule("Создайте расписание")
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(50.dp),
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.padding(vertical = 5.dp, horizontal = 15.dp)
                ) {
                    FloatingActionButton(
                        onClick = { /*TODO*/ },
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
        text = schedule.Options?.scheduleEveningSettings?.eveningTitleText.toString()
    }

    Text(
        text = coloredTitleText(
            text,
            user
        ),
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