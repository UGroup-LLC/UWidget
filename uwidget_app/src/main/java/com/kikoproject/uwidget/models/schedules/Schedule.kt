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
    val Category: String)

/**
 * Стандартные настройки расписания
 *
 * @author Kiko
 */
fun defaultScheduleOption(): ScheduleOptions
{
    return ScheduleOptions(
        generalSettings = ScheduleGeneralSettings(
            backgroundColor = materialColors.background,
            borderColor = materialColors.primary,
            borderThickness = 2,
            isBorderVisible = true,
            textSize = 16
        ),
        scheduleMorningSettings = ScheduleMorningSettings(
            7,
            "Доброе утро, @%n@",
            morningVisible = true,
            beforeLesionVisible = true,
            nextPairVisible = true,
            showWentTimeVisible = false,
            showMap = false
        ),
        scheduleDayLesionSettings = ScheduleDayLesionSettings(
            lesionEndTitleVisible = true,
            nextLesionVisible = true,
            lesionCountVisible = true,
            homeTimeVisible = true,
            additionalInfoVisible = true
        ),
        scheduleDayRestSettings = ScheduleDayRestSettings(
            lesionStartTitleVisible = true,
            nextLesionVisible = true,
            restCountVisible = true,
            homeTimeVisible = true,
            additionalInfoVisible = true
        ),
        scheduleEveningSettings = ScheduleEveningSettings(
            eveningTitleText ="Хорошего вечера, @%n@",
            eveningTitleVisible = true,
            allScheduleVisible = true,
            additionalInfoVisible = true
        )
    )
}