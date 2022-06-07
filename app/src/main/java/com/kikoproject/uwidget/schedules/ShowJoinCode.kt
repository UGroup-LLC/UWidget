package com.kikoproject.uwidget.schedules

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.main.curSchedule

@Composable
fun ShowJoinCode() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(horizontal = 30.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(15.dp),
            contentColor = MaterialTheme.colors.background,
            backgroundColor = MaterialTheme.colors.background,
            border = BorderStroke(1.dp, MaterialTheme.colors.primary)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(10.dp)) {

                Text(
                    "Ваш код расписания",
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colors.surface,
                )
                Text(
                    curSchedule?.JoinCode.toString(),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.caption,
                    fontSize = 32.sp,
                    color = MaterialTheme.colors.surface,
                )
            }
        }
    }
}