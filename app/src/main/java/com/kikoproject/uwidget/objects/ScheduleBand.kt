package com.kikoproject.uwidget.objects

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.main.days
import com.kikoproject.uwidget.models.schedules.Schedule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleBand(schedule: Schedule) {
    Column() {
        LazyRow() {
            items(6) { dayOfWeek ->
                if (schedule.Schedule[dayOfWeek.toString()] != null) {
                    Card(
                        modifier = Modifier
                            .padding(10.dp),
                        border = BorderStroke(
                            1.dp,
                            color = MaterialTheme.colors.surface.copy(0.3f)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colors.surface.copy(
                                alpha = 0.05f
                            )
                        )
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                            Text(
                                modifier = Modifier.padding(10.dp),
                                text = days[dayOfWeek],
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colors.surface.copy(0.6f)
                            )

                            Divider(
                                color = MaterialTheme.colors.surface.copy(0.2f),
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .padding(bottom = 10.dp)
                            )

                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp)
                            ) {
                                schedule.Schedule[dayOfWeek.toString()]!!.forEachIndexed { id, text ->
                                    Text(
                                        text = if (schedule.Time.lastIndex >= id) "${id + 1} (${
                                            schedule.Time[id].replace(
                                                "..",
                                                " - "
                                            )
                                        }): $text" else "${id + 1} (TE) $text",
                                        color = MaterialTheme.colors.surface.copy(0.4f),
                                        textAlign = TextAlign.Start
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}