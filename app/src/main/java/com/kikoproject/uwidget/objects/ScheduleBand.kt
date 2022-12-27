package com.kikoproject.uwidget.objects

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.main.days
import com.kikoproject.uwidget.models.schedules.Schedule


@Composable
fun ScheduleBand(schedule: Schedule) {
    LazyColumn(modifier = Modifier.clip(RoundedCornerShape(32.dp,32.dp,0.dp,0.dp))// this makes it scrollable
        .padding(horizontal = 16.dp),
        content = {
            items(6) {
                var mText = ""
                schedule.Schedule[it.toString()]!!.forEachIndexed { id, text ->
                    mText += if (schedule.Time.lastIndex >= id) "${id + 1} (${
                        schedule.Time[id].replace(
                            "..",
                            " - "
                        )
                    }): $text \n\n" else "${id + 1} (TE) $text"
                }

                ExpandableTextHelper(
                    modifier = Modifier.fillMaxWidth(),
                    cardColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                    titleSize = 14.sp,
                    titleColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                    fontSize = 14.sp,
                    title = days[it],
                    text = mText
                )
            }
        })
}




