package com.kikoproject.uwidget.models.schedules

import androidx.compose.ui.text.font.FontWeight
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kikoproject.uwidget.main.materialColors
import com.kikoproject.uwidget.models.schedules.options.*

@Entity(tableName = "schedules")
data class Schedule(
    @PrimaryKey val ID: String,
    val Name: String,
    val AdminID: String,
    val UsersID: List<String>,
    val Schedule: Map<String, MutableList<String>>,
    val JoinCode: String?,
    val Time: List<String>,
    val Category: String,
    val Options: ScheduleOptions?
)


fun DefaultScheduleOption(): ScheduleOptions
{
    return ScheduleOptions(
        generalSettings = ScheduleGeneralSettings(
            materialColors.background,
            false,
            materialColors.primary,
            2,
            materialColors.primary,
            FontWeight.Normal,
            materialColors.primary.copy(0.3f)
        ),
        scheduleMorningSettings = ScheduleMorningSettings(
            7,
            "Доброе утро, %n",
            true,
            true,
            true,
            false,
            false
        ),
        scheduleDayLesionSettings = ScheduleDayLesionSettings(
            true,
            true,
            true,
            true,
            true
        ),
        scheduleDayRestSettings = ScheduleDayRestSettings(
            true,
            true,
            true,
            true,
            true
        ),
        scheduleEveningSettings = ScheduleEveningSettings(
            "Хорошего вечера, %n",
            true,
            true,
            true
        )
    )
}