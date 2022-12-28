package com.kikoproject.uwidget.objects.home_widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.kikoproject.uwidget.main.curSchedule
import com.kikoproject.uwidget.main.options
import com.kikoproject.uwidget.objects.schedules.ScheduleBodyCard
import com.kikoproject.uwidget.objects.schedules.TitleSchedule
import com.kikoproject.uwidget.objects.text.colorize
import com.kikoproject.uwidget.time.TimeZone
import com.kikoproject.uwidget.widget.textThemeWidget

@Composable
fun HomeWidgetPreview(timeZone: TimeZone) {
    Card( // content
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 45.dp, vertical = 20.dp)
        ) {
            Card(
                shape = RoundedCornerShape(45.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(0.25f)
                )
            ) {
                if (curSchedule != null) {
                    TitleSchedule(
                        schedule = curSchedule!!,
                        timeZone = timeZone
                    )
                } else {
                    TitleSchedule("Создайте расписание")
                }
            }
            ScheduleBodyCard(schedule = curSchedule!!, timeZone)
        }
    }
}

@Preview
@Composable
fun HomeWidgetPreview() {
    textThemeWidget =
        if (ColorUtils.calculateLuminance(options!!.generalSettings.backgroundColor.toArgb()) < 0.5) {
            Color(0xFFEDF2F8)
        } else {
            Color(0xFF000000)
        }

    Card( // content
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = options!!.generalSettings.backgroundColor),
        border = BorderStroke(
            ((options!!.generalSettings.borderThickness.toInt() + 15) / 5).dp, // Если не выключена обводка то делаем ее
            if (options!!.generalSettings.isBorderVisible == true) {
                options!!.generalSettings.borderColor
            } else {
                Color.Black.copy(0.2f)
            }
        )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 45.dp, vertical = 20.dp)
        ) {
            Card(
                shape = RoundedCornerShape(45.dp),
                colors = CardDefaults.cardColors(
                    containerColor = options!!.generalSettings.borderColor.copy(0.25f)
                )
            ) {
                TitleSchedule("Заголовок", textThemeWidget)
            }
            PreviewBody()
        }
    }
}

@Composable
fun PreviewBody() {

    Spacer(modifier = Modifier.padding(4.dp))
    Text(
        text = "Время до пары",
        color = textThemeWidget,
        modifier = Modifier.fillMaxWidth()
    )
    Text(
        text = "@00:12@".colorize(
            colorPrimary = options!!.generalSettings.borderColor,
            colorOnSurface = textThemeWidget
        ),
        color = textThemeWidget,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.padding(4.dp))
    Text(
        text = ("@09:00 - 09:45@" +
                "\n" +
                "Маленький текст" +
                "\n\n" +
                "@9:55 - 10:30@" +
                "\n" +
                "Средний текст занятия в виджете" +
                "\n\n" +
                "@10:30 - 11:15@" +
                "\n" +
                "Большой текст занятия в виджете (здесь может быть группа)").colorize(
            colorPrimary = options!!.generalSettings.borderColor,
            colorOnSurface = textThemeWidget
        ),
        color = textThemeWidget,
        modifier = Modifier.fillMaxWidth()
    )
}