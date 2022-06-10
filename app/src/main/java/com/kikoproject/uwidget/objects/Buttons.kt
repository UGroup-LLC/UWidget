package com.kikoproject.uwidget.objects

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.main.curSchedule
import com.kikoproject.uwidget.main.curUser
import com.kikoproject.uwidget.main.navController
import com.kikoproject.uwidget.main.prefs
import com.kikoproject.uwidget.models.User
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.networking.getNextUserSchedule
import com.kikoproject.uwidget.networking.outFromSchedule

@Composable
fun ScheduleButton(schedule: Schedule, isAdmin: Boolean, mySheduleAdmin: List<Schedule>, mySheduleUser: List<Schedule>) {
    Button(
        onClick = {
            prefs.edit().putString(schedule.ID, "null").apply()
            curSchedule = schedule
            navController.popBackStack()
        },
        shape = RoundedCornerShape(17.dp),
        border = BorderStroke(0.dp, Color.Transparent),
        elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary.copy(
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
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.surface,
                modifier = Modifier.weight(4f)
            )
            FloatingActionButton(
                onClick = {
                    if (isAdmin) {
                        curSchedule = schedule
                        navController.navigate(ScreenNav.EditScheduleMenuNav.route)
                    } else {
                        val nextSchedule = getNextUserSchedule(mySchedulesUser = mySheduleUser, mySchedulesAdmin = mySheduleAdmin)
                        if (nextSchedule != null) {
                            prefs.edit().putString(nextSchedule.ID, "null").apply()
                            curSchedule = nextSchedule
                            navController.popBackStack()
                        } else {
                            navController.navigate(ScreenNav.ScheduleChooseNav.route)
                        }
                        outFromSchedule(schedule = schedule, userId = curUser.Id)

                    }
                },
                modifier = Modifier
                    .requiredSize(45.dp)
                    .weight(0.75f),
                backgroundColor = MaterialTheme.colors.primary,
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
            elevation = 0.dp,
            backgroundColor = MaterialTheme.colors.primary.copy(
                0.15f
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
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.surface,
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
                    backgroundColor = MaterialTheme.colors.primary,
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


@Composable
fun StandardButton(content: () -> Unit, text: String) {
    Button(
        onClick = { content() },
        shape = RoundedCornerShape(17.dp),
        border = BorderStroke(0.dp, Color.Transparent),
        elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary.copy(
                0.15f
            )
        ),
        modifier = Modifier.fillMaxWidth(0.9f)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.button,
            color = MaterialTheme.colors.surface,
            modifier = Modifier.weight(4f)
        )
    }
}

@Composable
fun StandardButton(content: () -> Unit, text: String, icon: ImageVector) {
    Button(
        onClick = { content() },
        shape = RoundedCornerShape(17.dp),
        border = BorderStroke(0.dp, Color.Transparent),
        elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary.copy(
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
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.surface,
                modifier = Modifier.weight(4f)
            )

            Icon(
                imageVector = icon,
                modifier = Modifier.padding(12.dp),
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
            )
        }
    }
}

@Composable
fun StandardButton(content: () -> Unit, text: String, icon: Painter) {
    val MaterialTheme.colors = MaterialTheme.colors
    Button(
        onClick = {
            content()
        },
        shape = RoundedCornerShape(17.dp),
        border = BorderStroke(0.dp, Color.Transparent),
        elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary.copy(
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
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.surface,
                modifier = Modifier.weight(4f)
            )

            Icon(
                painter = icon,
                modifier = Modifier.padding(12.dp),
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
            )
        }
    }
}