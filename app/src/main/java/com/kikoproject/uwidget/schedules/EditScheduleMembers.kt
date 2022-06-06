package com.kikoproject.uwidget.schedules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.main.curSchedule
import com.kikoproject.uwidget.main.navController
import com.kikoproject.uwidget.models.User
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.networking.MembersOnlineContent
import com.kikoproject.uwidget.objects.BackHeader
import com.kikoproject.uwidget.objects.StandardButton
import com.kikoproject.uwidget.objects.UserButton

@Composable
// Меню удаления мемберов расписания
fun EditScheduleMembers() {
    MembersOnlineContent(schedule = curSchedule, content = { users ->

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
                BackHeader("Пользователи")
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(users) { user ->
                        UserButton(schedule = curSchedule, user = user)
                    }
                }
            }
        }
    })
}