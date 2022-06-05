package com.kikoproject.uwidget.schedules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.main.curSchedule
import com.kikoproject.uwidget.main.curSchedules
import com.kikoproject.uwidget.main.navController
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.objects.BackHeader
import com.kikoproject.uwidget.objects.ScheduleButton
import com.kikoproject.uwidget.objects.StandardButton

@Composable
// Меню админ панели расписания где будет редактирование расписание, удаление members,
// Регенерировать код приглашения
fun EditSheduleMenu(){
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
            BackHeader("Админ панель")
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally){
                item {
                    StandardButton(content = {navController.navigate(ScreenNav.EditScheduleNav.route)},text = "Редактировать", icon = painterResource(R.drawable.ic_pencil))
                }
                item{
                    StandardButton(content = {navController.navigate(ScreenNav.EditScheduleNav.route)}, text = "Управление участниками", icon = painterResource(R.drawable.ic_account_circle))
                }
                item{
                    if(curSchedule.JoinCode != "null") ShowJoinCode()
                }
            }
        }
    }
}