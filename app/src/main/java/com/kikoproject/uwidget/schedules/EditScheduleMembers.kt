package com.kikoproject.uwidget.schedules

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.R
import com.kikoproject.uwidget.dialogs.ShowErrorDialog
import com.kikoproject.uwidget.main.curSchedule
import com.kikoproject.uwidget.networking.MembersOnlineContent
import com.kikoproject.uwidget.objects.BackHeader
import com.kikoproject.uwidget.objects.buttons.UserButton
import com.kikoproject.uwidget.objects.cards.CardIllustration
import com.kikoproject.uwidget.objects.cards.RoundedCard

/**
 * Меню где отображаются мемберы расписания и кнопки для их удаления
 *
 * @author Kiko
 */
@Composable
fun EditScheduleMembers() {
    if (curSchedule != null) {
        MembersOnlineContent(schedule = curSchedule!!, content = { users ->

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
                    if (users.isNotEmpty()) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            items(users) { user ->
                                UserButton(schedule = curSchedule!!, user = user)
                            }
                        }
                    } else {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CardIllustration(
                                    R.drawable.ic_undraw_empty,
                                    6,
                                    3,
                                    border = BorderStroke(0.dp, color = Color.Transparent)
                                )
                                RoundedCard(textColor = MaterialTheme.colors.surface, text = "В галактике никого нет", spacing = 2.sp)

                            }
                        }
                    }
                }
            }
        })
    } else {
        ShowErrorDialog(text = "Schedule error", needButton = true)
    }
}
