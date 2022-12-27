package com.kikoproject.uwidget.objects.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.dialogs.ShowSchedulePreviewDialog
import com.kikoproject.uwidget.main.*
import com.kikoproject.uwidget.models.User
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.networking.getNextUserSchedule
import com.kikoproject.uwidget.networking.outFromSchedule
import com.kikoproject.uwidget.objects.Avatar

/**
 * Кнопка расписания, при нажатии меняет curSchedule на расписание в кнопке
 * Имеет в себе кнопку для удаления или управления расписанием
 *
 * @param schedule расписание кнопки
 * @param isAdmin является ли пользователь администратором расписания
 * @param scheduleAdmin расписание где пользователь администратор
 * @param scheduleUser расписание где пользователь простой юзер
 *
 * @author Kiko
 */
@Composable
fun ScheduleButton(
    schedule: Schedule,
    isAdmin: Boolean,
    scheduleAdmin: List<Schedule>,
    scheduleUser: List<Schedule>
) {
    val showDialog = remember { mutableStateOf(false) }
    if (showDialog.value) {
        ShowSchedulePreviewDialog(showDialog, schedule)
    }

    Button(
        onClick = {
            prefs?.edit()?.putString(schedule.ID, "null")?.apply()
            chosenByUserSchedule = schedule
            showDialog.value = true
        },
        shape = RoundedCornerShape(17.dp),
        border = BorderStroke(0.dp, Color.Transparent),
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(
                0.15f
            )
        ),
        modifier = Modifier.fillMaxWidth(0.9f)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = schedule.Name,
                modifier = Modifier.weight(4f),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            FloatingActionButton(
                onClick = {
                    if (isAdmin) {
                        curSchedule = schedule
                        navController.navigate(ScreenNav.EditScheduleMenuNav.route)
                    } else {
                        val nextSchedule = getNextUserSchedule(
                            mySchedulesUser = scheduleUser,
                            mySchedulesAdmin = scheduleAdmin
                        )
                        if (nextSchedule != null) {
                            prefs?.edit()?.putString(nextSchedule.ID, "null")?.apply()
                            curSchedule = nextSchedule
                            navController.popBackStack()
                        } else {
                            navController.navigate(ScreenNav.Dashboard.route)
                        }
                        outFromSchedule(schedule = schedule, userId = curUser.Id)
                    }
                },
                modifier = Modifier
                    .requiredSize(45.dp)
                    .weight(0.75f),
                containerColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(15.dp)
            ) {
                val icon =
                    if (isAdmin) R.drawable.ic_admin_panel_settings else R.drawable.ic_delete
                Icon(
                    painter = painterResource(id = icon),
                    modifier = Modifier.padding(12.dp),
                    contentDescription = null,
                    tint = Color.Black.copy(0.4f),
                )
            }
        }
    }
}

/**
 * Кнопка пользователя, имеет в себе аватар пользователя и кнопку удаления из расписания
 *
 * @param schedule расписание
 * @param user пользователь
 *
 * @author Kiko
 */
@Composable
fun UserButton(schedule: Schedule, user: User) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(
            user = user,
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .width(58.dp)
                .height(58.dp)
        )
        Card(
            shape = RoundedCornerShape(17.dp),
            border = BorderStroke(0.dp, Color.Transparent),
            elevation = CardDefaults.cardElevation(0.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(
                    0.15f
                )
            ),
            modifier = Modifier.fillMaxWidth(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(7.dp)
            ) {

                Text(
                    text = user.Surname + " " + user.Name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxWidth()
                        .padding(start = 5.dp),
                    textAlign = TextAlign.Start
                )
                FloatingActionButton(
                    onClick = {
                        outFromSchedule(schedule, user.Id)
                        navController.popBackStack()
                        navController.navigate(ScreenNav.EditMembersNav.route)
                    },
                    modifier = Modifier
                        .requiredSize(45.dp)
                        .weight(0.75f),
                    containerColor = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        modifier = Modifier.padding(12.dp),
                        contentDescription = null,
                        tint = Color.Black.copy(0.4f),
                    )
                }
            }
        }
    }
}

/**
 * Стандартная кнопка в приложении
 *
 * @param content указывает что необходимо сделать при нажатии на кнопку
 * @param icon встроенная иконка кнопки
 *
 * @author Kiko
 */
@Composable
fun StandardButton(content: () -> Unit, text: String, icon: ImageVector) {
    Button(
        onClick = { content() },
        shape = RoundedCornerShape(17.dp),
        border = BorderStroke(0.dp, Color.Transparent),
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(
                0.15f
            )
        ),
        modifier = Modifier.fillMaxWidth(0.9f)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.weight(4f),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )

            Icon(
                imageVector = icon,
                modifier = Modifier.padding(12.dp),
                contentDescription = null
            )
        }
    }
}

/**
 * Стандартная кнопка в приложении
 *
 * @param content указывает что необходимо сделать при нажатии на кнопку
 * @param icon не встроенная иконка кнопки
 *
 * @author Kiko
 */
@Composable
fun StandardButton(content: () -> Unit, text: String, icon: Painter) {
    Button(
        onClick = {
            content()
        },
        shape = RoundedCornerShape(17.dp),
        border = BorderStroke(0.dp, Color.Transparent),
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(
                0.15f
            )
        ),
        modifier = Modifier.fillMaxWidth(0.9f)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.weight(4f),
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Icon(
                painter = icon,
                modifier = Modifier.padding(12.dp),
                contentDescription = null,
            )
        }
    }
}