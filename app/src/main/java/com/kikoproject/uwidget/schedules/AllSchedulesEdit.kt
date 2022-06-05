package com.kikoproject.uwidget.schedules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kikoproject.uwidget.main.curSchedules
import com.kikoproject.uwidget.main.curUser
import com.kikoproject.uwidget.main.navController
import com.kikoproject.uwidget.main.roomDb
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.objects.BackHeader
import com.kikoproject.uwidget.objects.ScheduleButton
import com.kikoproject.uwidget.objects.StandardButton

@Composable
fun AllSchedulesActivity(){
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
            BackHeader("Мои расписания")
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally){
                items(curSchedules) { schedule ->
                    ScheduleButton(schedule, true)
                }
                item {
                    StandardButton(content = { navController.navigate(ScreenNav.AddScheduleNav.route)},text = "Создать расписание", Icons.Rounded.Add)
                }
                item{
                    StandardButton(content = { navController.navigate(ScreenNav.JoinToScheduleNav.route)},text = "Присоедениться к расписанию",  Icons.Rounded.Lock)
                }
            }
        }
    }
}