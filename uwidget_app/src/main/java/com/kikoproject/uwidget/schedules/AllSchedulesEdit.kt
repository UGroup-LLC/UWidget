package com.kikoproject.uwidget.schedules

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.main.curUser
import com.kikoproject.uwidget.main.navController
import com.kikoproject.uwidget.navigation.ScreenNav
import com.kikoproject.uwidget.networking.OnlineContent
import com.kikoproject.uwidget.objects.BackHeader
import com.kikoproject.uwidget.objects.buttons.ScheduleButton
import com.kikoproject.uwidget.objects.buttons.StandardButton
import com.kikoproject.uwidget.objects.cards.CardIllustration
import com.kikoproject.uwidget.objects.cards.RoundedCard

/**
 * Содержит в себе все расписания пользователя и их настройку
 * @author Kiko
 */
@Composable
fun AllSchedulesActivity() {
    OnlineContent(user = curUser, content = { myScheduleUser, myScheduleAdmin ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 30.dp, vertical = 10.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                BackHeader("Мои расписания")
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        StandardButton(
                            content = { navController.navigate(ScreenNav.AddScheduleNav.route) },
                            text = "Создать расписание",
                            Icons.Rounded.Add
                        )
                    }
                    item {
                        StandardButton(
                            content = { navController.navigate(ScreenNav.JoinToScheduleNav.route) },
                            text = "Присоедениться к расписанию",
                            Icons.Rounded.Lock
                        )
                    }
                    item {
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .padding(5.dp),
                            color = MaterialTheme.colorScheme.primary.copy(0.2f),
                            thickness = 1.dp
                        )
                    }
                    if(myScheduleAdmin.isNotEmpty() || myScheduleUser.isNotEmpty()) {
                        // Раписания где юзер админ
                        items(myScheduleAdmin) { schedule ->
                            ScheduleButton(schedule, true, myScheduleAdmin, myScheduleUser)
                        }
                        // Расписания где юзер это простой смертный
                        items(myScheduleUser) { schedule ->
                            ScheduleButton(schedule, false, myScheduleAdmin, myScheduleUser)
                        }
                    }
                    else{
                        item {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    CardIllustration(
                                        R.drawable.ic_undraw_empty,
                                        6,
                                        3,
                                        border = BorderStroke(0.dp, color = Color.Transparent)
                                    )
                                    RoundedCard(textColor = MaterialTheme.colorScheme.onSurface, text = "В галактике нет расписаний", spacing = 2.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    })
}