package com.kikoproject.uwidget.objects

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.main.curSchedule
import com.kikoproject.uwidget.main.navController
import com.kikoproject.uwidget.models.schedules.Schedule
import com.kikoproject.uwidget.navigation.ScreenNav

@Composable
fun ScheduleButton(schedule: Schedule, isAdmin: Boolean) {
    Button(
        onClick = {
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
                    curSchedule = schedule
                    navController.navigate(ScreenNav.EditScheduleMenuNav.route)
                },
                modifier = Modifier
                    .requiredSize(45.dp)
                    .weight(0.75f),
                backgroundColor = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(15.dp)
            ) {
                val icon =
                    if (isAdmin) R.drawable.ic_admin_panel_settings else R.drawable.ic_account_circle
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
fun StandardButton(content: () -> Unit, text: String){
    Button(
        onClick = {content()},
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
fun StandardButton(content: () -> Unit, text: String, icon: ImageVector){
    Button(
        onClick = {content()},
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
fun StandardButton(content: () -> Unit, text: String, icon: Painter){
    Button(
        onClick = {content()},
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