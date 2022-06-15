package com.kikoproject.uwidget.models.schedules.options

/**
 * Класс данных отвечающий за все настройки
 *
 * @author Kiko
 */
data class ScheduleOptions(
    val generalSettings: ScheduleGeneralSettings,
    val scheduleMorningSettings: ScheduleMorningSettings,
    val scheduleDayLesionSettings: ScheduleDayLesionSettings,
    val scheduleDayRestSettings: ScheduleDayRestSettings,
    val scheduleEveningSettings: ScheduleEveningSettings
)
